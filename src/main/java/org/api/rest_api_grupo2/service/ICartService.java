package org.api.rest_api_grupo2.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.NFTCartItemRequest;
import org.api.rest_api_grupo2.dto.response.CartResponseDTO;
import org.api.rest_api_grupo2.dto.response.CheckoutResponse;
import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.dto.response.NftTokenInCartDTO;
import org.api.rest_api_grupo2.model.NFTToken;

public interface ICartService {
    public MessageResponseDto createCart(NFTCartItemRequest itemRequest) throws BadRequestException;
    public MessageResponseDto updateCart(Long cartId, List<NFTCartItemRequest> items) throws BadRequestException;
    public CartResponseDTO getItems(Long cartId);
    MessageResponseDto removeNFT(Long cartId, Long nftTokenId) throws BadRequestException;
    public MessageResponseDto deleteCart(Long cartId);
    public CheckoutResponse checkoutCart(Long cartId) throws BadRequestException;
}
