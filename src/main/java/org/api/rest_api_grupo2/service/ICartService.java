package org.api.rest_api_grupo2.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.response.CheckoutResponse;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.NFTToken;

public interface ICartService {
    public MessageResponseDto createCart() throws BadRequestException;
    public MessageResponseDto updateCart(Long cartId);
    public List<NFTToken> getItems(Long cartId);
    public MessageResponseDto removeNFT(Long nftTokenId) throws BadRequestException;
    public MessageResponseDto deleteCart(Long cartId);
    public CheckoutResponse checkoutCart() throws BadRequestException;
}
