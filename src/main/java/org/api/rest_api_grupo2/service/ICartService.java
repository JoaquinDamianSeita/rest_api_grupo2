package org.api.rest_api_grupo2.service;

import java.util.List;

import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.NFTToken;

public interface ICartService {
    public Cart getCartByUserId(Long userId);
    public Cart createCartIfNotExists(Long userId);
    public Cart addNFTToken(Long userId, Long nftTokenId);
    public Cart removeNFTFromCart(Long userId, Long nftTokenId);
    public void clearCart(Long userId);
    public List<NFTToken> listCartTokens(Long userId);
}
