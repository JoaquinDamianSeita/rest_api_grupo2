package org.api.rest_api_grupo2.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.api.rest_api_grupo2.dto.request.SaleCreateRequest;
import org.api.rest_api_grupo2.dto.response.NFTResponse;
import org.api.rest_api_grupo2.dto.response.SaleResponse;
import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.ImageUrl;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.model.Sale;
import org.api.rest_api_grupo2.model.SaleToken;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.model.serializables.SaleTokenId;
import org.api.rest_api_grupo2.repository.CartRepository;
import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.repository.SaleRepository;
import org.api.rest_api_grupo2.repository.SaleTokenRepository;
import org.api.rest_api_grupo2.repository.UserRepository;
import org.api.rest_api_grupo2.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements ISaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaleTokenRepository saleTokenRepository;

    @Autowired
    private NFTTokenRepository nftTokenRepository;

    @Autowired
    private CartRepository cartRepository;
    

@Override
public Sale createSale(SaleCreateRequest saleRequest) {
    // Obtener el comprador
    User buyer = userRepository.findById(saleRequest.getBuyerId())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    // Obtener el carrito confirmado (si es parte del request)
    Cart cart = cartRepository.findById(saleRequest.getCartId())
        .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

    // Crear la venta
    Sale sale = new Sale();
    sale.setUser(buyer);
    sale.setCart(cart); // Asociar el carrito a la venta
    sale.setSaleDate(LocalDateTime.now());

    // Guardar la venta para generar el ID
    final Sale savedSale = saleRepository.save(sale);

    // Procesar los tokens
    List<SaleToken> saleTokens = cart.getTokens().stream()
        .map(token -> createSaleToken(savedSale, token.getId()))
        .toList();

    // Calcular el precio total de la venta
    double totalSalePrice = saleTokens.stream()
        .mapToDouble(SaleToken::getSalePrice)
        .sum();
    savedSale.setSalePrice(totalSalePrice);

    // Guardar las relaciones SaleToken
    saleTokenRepository.saveAll(saleTokens);

    // Actualizar disponibilidad de los tokens vendidos
    saleTokens.forEach(saleToken -> {
        NFTToken token = saleToken.getToken();
        token.setAvailable(false);
        nftTokenRepository.save(token);
    });

    // Guardar la venta actualizada con el precio total
    saleRepository.save(savedSale);

    return savedSale;
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
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

@Override
public SaleResponse toResponse(Sale sale) {
    List<NFTResponse> nftResponses = saleTokenRepository.findBySale(sale).stream()
        .map(saleToken -> {
            NFTToken token = saleToken.getToken();
            List<String> imageUrls = token.getImageUrls().stream()
                .map(ImageUrl::getUrl) // Usa el método `getUrl()` para obtener las URLs
                .collect(Collectors.toList());

            return new NFTResponse(
                token.getId(),
                token.getTitle(),
                token.getDescription(),
                token.getPrice(),
                token.getArtType().toString(), // Convierte el Enum a String
                token.getPhysicalPieces(),
                token.getAvailable(),
                imageUrls
            );
        })
        .toList();

    return new SaleResponse(
        sale.getId(),
        sale.getCart().getId(), // Si tienes esta relación en Sale
        sale.getSalePrice(),
        sale.getSaleDate(),
        nftResponses
    );
}

@Override
public List<SaleResponse> getSalesByRole(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    List<Sale> sales;

    if ("ARTIST".equalsIgnoreCase(user.getRole().getName())) {
        sales = saleRepository.findAll().stream()
            .filter(sale -> isSaleRelatedToArtist(sale, userId))
            .collect(Collectors.toList());
    } else if ("BUYER".equalsIgnoreCase(user.getRole().getName())) {
        sales = saleRepository.findByUserId(userId);
    } else {
        throw new IllegalArgumentException("Rol no reconocido");
    }

    return sales.stream().map(this::toResponse).toList();
}


private boolean isSaleRelatedToArtist(Sale sale, Long artistId) {
    List<SaleToken> saleTokens = saleTokenRepository.findBySale(sale);

    return saleTokens.stream()
        .anyMatch(saleToken -> saleToken.getToken().getUser().getId().equals(artistId));
}


}
