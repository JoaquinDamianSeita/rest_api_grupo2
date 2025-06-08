package org.api.rest_api_grupo2.service.impl;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.SaleCreateRequest;
import org.api.rest_api_grupo2.dto.response.SaleResponse;
import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.model.Sale;
import org.api.rest_api_grupo2.model.SaleToken;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.model.serializables.SaleTokenId;
import org.api.rest_api_grupo2.repository.CartRepository;
import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.repository.SaleRepository;
import org.api.rest_api_grupo2.repository.SaleTokenRepository;
import org.api.rest_api_grupo2.service.ISaleService;
import org.api.rest_api_grupo2.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements ISaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleTokenRepository saleTokenRepository;

    @Autowired
    private NFTTokenRepository nftTokenRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public SaleResponse.NFTSaleResponse createSale(SaleCreateRequest saleRequest) throws BadRequestException {
        // Get authenticated user (buyer)
        User buyer = userService.getAutheticatedUser();

        if (!"BUYER".equalsIgnoreCase(buyer.getRole().getName())) {
            throw new BadRequestException("Solo los compradores pueden realizar compras");
        }

        // Fetch and validate cart with the NFTs
        Cart cart = cartRepository.findById(saleRequest.getCartId())
                .orElseThrow(() -> new BadRequestException("Carrito no encontrado"));

        if (!cart.getUser().getId().equals(buyer.getId())) {
            throw new BadRequestException("No tienes permiso para acceder a este carrito");
        }

        if (cart.getTokens() == null || cart.getTokens().isEmpty()) {
            throw new BadRequestException("El carrito está vacío");
        }

        try {
            // Create sale record
            Sale sale = new Sale();
            sale.setUser(buyer);
            sale.setSaleDate(LocalDateTime.now());
            Sale savedSale = saleRepository.save(sale);

            // Process each NFT in the cart
            List<SaleToken> saleTokens = new ArrayList<>();
            for (NFTToken token : cart.getTokens()) {
                if (!token.getAvailable()) {
                    throw new BadRequestException("El NFT " + token.getTitle() + " ya no está disponible");
                }

                // Create sale token
                SaleToken saleToken = new SaleToken();
                SaleTokenId saleTokenId = new SaleTokenId();
                saleTokenId.setSaleId(savedSale.getId());
                saleTokenId.setTokenId(token.getId());

                saleToken.setId(saleTokenId);
                saleToken.setSale(savedSale);
                saleToken.setToken(token);
                saleToken.setSalePrice(token.getPrice());

                saleTokens.add(saleToken);

                // Mark NFT as sold
                token.setAvailable(false);
                nftTokenRepository.save(token);
            }

            // Save all sale tokens
            saleTokenRepository.saveAll(saleTokens);

            // Get the first token for the response
            NFTToken firstToken = saleTokens.get(0).getToken();

            // Build the response
            SaleResponse.NFTSaleResponse response = new SaleResponse.NFTSaleResponse();
            response.setId(firstToken.getId());
            response.setTitle(firstToken.getTitle());
            response.setDescription(firstToken.getDescription());
            response.setPrice(firstToken.getPrice());
            response.setArtType(firstToken.getArtType().name());
            response.setPhysicalPieces(firstToken.getPhysicalPieces());
            response.setSold(true);

            // Set artist info
            User artist = firstToken.getUser();
            SaleResponse.ArtistResponse artistResponse = new SaleResponse.ArtistResponse();
            artistResponse.setFirstName(artist.getFirstName());
            artistResponse.setLastName(artist.getLastName());
            response.setArtist(artistResponse);

            // Set image URLs
            if (firstToken.getImageUrls() != null && !firstToken.getImageUrls().isEmpty()) {
                List<String> imageUrls = firstToken.getImageUrls().stream()
                        .map(url -> url.getUrl())
                        .collect(Collectors.toList());
                response.setImageUrls(imageUrls);
            } else {
                response.setImageUrls(Collections.singletonList("img.com"));
            }

            return response;
        } catch (Exception e) {
            throw new BadRequestException("Error al procesar la compra: " + e.getMessage());
        }
    }

    // Creamos un SaleToken a partir de una venta y un token ID
    private SaleToken createSaleToken(Sale sale, Long tokenId) {
        NFTToken token = nftTokenRepository.findById(tokenId)
                .orElseThrow(() -> new IllegalArgumentException("Token no encontrado o no disponible"));

        if (!token.getAvailable()) {
            throw new IllegalArgumentException("Token no disponible para la venta");
        }

        SaleTokenId saleTokenId = new SaleTokenId();
        saleTokenId.setSaleId(sale.getId());
        saleTokenId.setTokenId(token.getId());

        SaleToken saleToken = new SaleToken();
        saleToken.setId(saleTokenId);
        saleToken.setSale(sale);
        saleToken.setToken(token);
        saleToken.setSalePrice(token.getPrice());

        return saleToken;
    }

    @Override
    public List<SaleResponse> getSalesByUser() throws BadRequestException {
        User user = userService.getAutheticatedUser();

        if ("BUYER".equalsIgnoreCase(user.getRole().getName())) {
            // For buyers, get all sales where they are the buyer
            List<Sale> buyerSales = saleRepository.findByUserId(user.getId());
            return buyerSales.stream()
                    .map(this::mapToSaleResponse)
                    .collect(Collectors.toList());
        } else if ("ARTIST".equalsIgnoreCase(user.getRole().getName())) {
            // For artists, get all sales where the NFTs belong to them
            List<Sale> allSales = saleRepository.findAll();
            return allSales.stream()
                    .filter(sale -> isSaleRelatedToArtist(sale, user.getId()))
                    .map(this::mapToSaleResponse)
                    .collect(Collectors.toList());
        }

        throw new BadRequestException("Rol de usuario no soportado");
    }

    private boolean isSaleRelatedToArtist(Sale sale, Long artistId) {
        List<SaleToken> saleTokens = saleTokenRepository.findBySale(sale);

        return saleTokens.stream()
                .anyMatch(saleToken -> saleToken.getToken().getUser().getId().equals(artistId));
    }

    private SaleResponse mapToSaleResponse(Sale sale) {
        SaleResponse response = new SaleResponse();
        response.setSaleId(sale.getId());
        response.setSaleDate(sale.getSaleDate());

        // Get all sale tokens for this sale
        List<SaleToken> saleTokens = saleTokenRepository.findBySale(sale);

        // Calculate total sale price
        double totalPrice = saleTokens.stream()
                .mapToDouble(SaleToken::getSalePrice)
                .sum();
        response.setSalePrice(totalPrice);

        // Map NFT details
        List<SaleResponse.NFTSaleResponse> nftResponses = saleTokens.stream()
                .map(saleToken -> {
                    NFTToken token = saleToken.getToken();
                    SaleResponse.NFTSaleResponse nftResponse = new SaleResponse.NFTSaleResponse();
                    nftResponse.setId(token.getId());

                    // Set artist info
                    User artist = token.getUser();
                    SaleResponse.ArtistResponse artistResponse = new SaleResponse.ArtistResponse();
                    artistResponse.setFirstName(artist.getFirstName());
                    artistResponse.setLastName(artist.getLastName());
                    nftResponse.setArtist(artistResponse);

                    // Set NFT details
                    nftResponse.setTitle(token.getTitle());
                    nftResponse.setDescription(token.getDescription());
                    nftResponse.setPrice(token.getPrice());
                    nftResponse.setReleaseDate(token.getReleaseDate());
                    nftResponse.setImageUrls(Collections.singletonList("img.com")); // Default image URL
                    nftResponse.setArtType(token.getArtType().name());
                    nftResponse.setPhysicalPieces(token.getPhysicalPieces());
                    nftResponse.setSold(true); // Since it's in a sale, it's sold

                    return nftResponse;
                })
                .collect(Collectors.toList());

        response.setNfts(nftResponses);

        // Set cart ID (assuming it's the same as sale ID for now, adjust if needed)
        response.setCartId(sale.getId());

        return response;
    }
}