package org.api.rest_api_grupo2.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.response.CheckoutResponse;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.exceptions.NotFoundException;
import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.model.Sale;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.repository.CartRepository;
import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.repository.SaleRepository;
import org.api.rest_api_grupo2.repository.UserRepository;
import org.api.rest_api_grupo2.service.ICartService;
import org.api.rest_api_grupo2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private NFTTokenRepository nftTokenRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private IUserService userService;

    @Override
    public MessageResponseDto createCart() throws BadRequestException{
        User user = userService.getAutheticatedUser();
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTokens(new ArrayList<>());
        cart.setCreatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return new MessageResponseDto("Carrito registrado con exito.");
    }

    @Override
    public MessageResponseDto updateCart(Long cartId){
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
        cartRepository.save(cart);
        return new MessageResponseDto("Carrito modificado con exito.");
    }

    @Override
    public List<NFTToken> getItems(Long cartId){
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
        return cart.getTokens();
    }

    @Override
    public MessageResponseDto deleteCart(Long cartId){
        cartRepository.deleteById(cartId);
        return new MessageResponseDto("Carrito eliminado con exito.");
    }

    @Override
    public MessageResponseDto removeNFT(Long nftTokenId) throws BadRequestException{
        User user = userService.getAutheticatedUser();
        Cart cart = cartRepository.findByUserId(user)
            .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
        NFTToken nft = nftTokenRepository.findById(nftTokenId)
            .orElseThrow(() -> new NotFoundException("NFT no encontrado"));
        cart.getTokens().remove(nft);
        cartRepository.save(cart);
        return new MessageResponseDto("Carrito modificado con exito.");
    }

    @Override
    public CheckoutResponse checkoutCart() throws BadRequestException{
        User user = userService.getAutheticatedUser();
        Cart cart = cartRepository.findByUserId(user)
            .orElseThrow(() -> new NotFoundException("Carrito no encontrado."));
        double total = cart.getTokens().stream().mapToDouble(NFTToken::getPrice).sum();

        return new CheckoutResponse("Compra confirmada con exito.", cart.getId(), total, cart.getConfirmedAt());
    }
}
