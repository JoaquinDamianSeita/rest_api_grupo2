package org.api.rest_api_grupo2.service;

import org.api.rest_api_grupo2.dto.request.NFTCreateRequest;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.dto.response.NFTResponse;

import java.util.List;

public interface INFTTokenService {
    NFTToken createNFT(NFTCreateRequest request);
    NFTToken updateNFT(Long id, NFTCreateRequest request);
    NFTResponse getNFTById(Long id);
    List<NFTToken> getAllNFTs();
    NFTResponse toResponse(NFTToken nft);
    void deleteNFT(Long id);



}
