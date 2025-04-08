package org.api.rest_api_grupo2.service.impl;

import java.util.List;

import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.repository.CartRepository;
import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.repository.UserRepository;
import org.api.rest_api_grupo2.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NFTTokenRepository nftTokenRepository;

    public Cart getCartByUserId(Long userId){
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("No se ha encontrado un carrito pasa el user id: " + userId));
    }

    public Cart createCartIfNotExists(Long userId){
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
                Cart nuevoCarrito = new Cart();
                nuevoCarrito.setUser(user);
                nuevoCarrito.setCreatedAt(LOcalTime.now);
                nuevoCarrito.setTokens(new ArrayList<>());
                return cartRepository.save(nuevoCarrito);
            })
    }

    public Cart addNFTToken(Long userId, Long nftTokenId){
        Cart cart = createCartIfNotExists(userId);
        NFTToken nftToken = nftTokenRepository.findById(nftTokenId)
            .orElseThrow(() -> new EntityNotFoundException("NFT no encontrado"));

        if(!cart.getTokens().contains(nftToken)){
            cart.getTokens().add(nftToken);
        }
        return cartRepository.save(cart);
    }

    public Cart removeNFTFromCart(Long userId, Long nftTokenId){
        Cart cart = getCartByUserId(userId);
        NFTToken nftToken = nftTokenRepository.findById(nftTokenId)
            .orElseThrow(() -> new EntityNotFoundException("NFT no encontrado"));

        cart.getTokens().remove(nftTokenId);
        return cartRepository.save(cart);
    }

    public void clearCart(Long userId){
        Cart cart = getCartByUserId(userId);
        cart.getTokens().clear();
        cartRepository.save(cart);
    }

    public List<NFTToken> listCartTokens(Long userId){
        Cart cart = getCartByUserId(userId);
        return cart.getTokens();
    }
}
