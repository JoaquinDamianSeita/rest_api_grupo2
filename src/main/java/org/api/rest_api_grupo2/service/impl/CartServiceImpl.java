package org.api.rest_api_grupo2.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.NFTCartItemRequest;
import org.api.rest_api_grupo2.dto.response.CartResponseDTO;
import org.api.rest_api_grupo2.dto.response.CheckoutResponse;
import org.api.rest_api_grupo2.dto.response.CreateResponse;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.dto.response.NftTokenInCartDTO;
import org.api.rest_api_grupo2.enums.ArtType;
import org.api.rest_api_grupo2.exceptions.NotFoundException;
import org.api.rest_api_grupo2.exceptions.UnprocessableEntityException;
import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.ImageUrl;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.repository.CartRepository;
import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.service.ICartService;
import org.api.rest_api_grupo2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private NFTTokenRepository nftTokenRepository;

    @Autowired
    private IUserService userService;

    @Override
    public CreateResponse createCart(NFTCartItemRequest itemRequest) throws NotFoundException, UnprocessableEntityException, BadRequestException {
        User user = userService.getAutheticatedUser();
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());

        List<NFTToken> nftList = new ArrayList<>();
        NFTToken nft = nftTokenRepository.findById(itemRequest.getNftId())
            .orElseThrow(() -> new NotFoundException("NFT no encontrado."));
        
        if(nft.getArtType() == ArtType.PHYSICAL){
            if(itemRequest.getPhysicalPieces() > nft.getPhysicalPieces()){
                throw new UnprocessableEntityException("No hay stock fisico suficiente de este NFT.");
            }
            nft.setPhysicalPieces(nft.getPhysicalPieces() - itemRequest.getPhysicalPieces());
        }

        nftList.add(nft);
        cart.setTokens(nftList);
        
        cartRepository.save(cart);
        nftTokenRepository.save(nft);
        return new CreateResponse("Carrito registrado con exito.", cart.getId());
    }

    @Override
    public MessageResponseDto updateCart(Long cartId, List<NFTCartItemRequest> items) throws BadRequestException{
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
        
        for(NFTCartItemRequest item : items){
            NFTToken nft = nftTokenRepository.findById(item.getNftId())
                .orElseThrow(() -> new BadRequestException("NFT no encontrado."));
            
            if(nft.getArtType() == ArtType.PHYSICAL){
                if(item.getPhysicalPieces() > nft.getPhysicalPieces()){
                    throw new BadRequestException("No hay stock fisico suficiente de este NFT.");
                }
                nft.setPhysicalPieces(nft.getPhysicalPieces() - item.getPhysicalPieces());
            }
            cart.getTokens().add(nft);
            nftTokenRepository.save(nft);
        }
        
        cartRepository.save(cart);
        return new MessageResponseDto("Se han agregado los items al carrito.");
    }

    @Override
    public CartResponseDTO getItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));

        List<NftTokenInCartDTO> nftTokens = cart.getTokens().stream().map(token -> {
            List<String> imageUrls = token.getImageUrls().stream()
                    .map(ImageUrl::getUrl)
                    .collect(Collectors.toList());

            return new NftTokenInCartDTO(
                    token.getId(),
                    token.getTitle(),
                    token.getPrice(),
                    imageUrls,
                    token.getPhysicalPieces(),
                    token.getArtType().name().equalsIgnoreCase("PHYSICAL") ? "Fisico" : "Digital"
            );
        }).collect(Collectors.toList());

        return new CartResponseDTO(cart.getId(), nftTokens);
    }

    @Override
    public MessageResponseDto deleteCart(Long cartId){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));

        cartRepository.deleteById(cart.getId());
        return new MessageResponseDto("Carrito eliminado con exito.");
    }

    @Override
    public MessageResponseDto removeNFT(Long cartId, Long nftTokenId) throws BadRequestException {
        // Obtener el carrito usando el cartId
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));

        NFTToken nft = nftTokenRepository.findById(nftTokenId)
                .orElseThrow(() -> new NotFoundException("NFT no encontrado"));
        if (!cart.getTokens().contains(nft)) {
            throw new BadRequestException("El NFT no está en el carrito.");
        }

        cart.getTokens().remove(nft);
        cartRepository.save(cart);
        return new MessageResponseDto("Item eliminado del carrito con exito.");
    }


    @Override
    public CheckoutResponse checkoutCart(Long cartId) throws BadRequestException{
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
        double total = cart.getTokens().stream().mapToDouble(NFTToken::getPrice).sum();

        cart.setConfirmedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return new CheckoutResponse("Compra confirmada con exito.", cart.getId(), total, cart.getConfirmedAt());
    }
}
