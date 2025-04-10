package org.api.rest_api_grupo2.controller;

import org.api.rest_api_grupo2.dto.response.MessageResponseDto;
import org.api.rest_api_grupo2.dto.response.NFTResponse;
import org.api.rest_api_grupo2.service.INFTTokenService;
import org.api.rest_api_grupo2.service.impl.NFTTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.api.rest_api_grupo2.dto.request.NFTCreateRequest;
import org.api.rest_api_grupo2.model.NFTToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nfts")
public class NFTTokenController {

    @Qualifier("NFTTokenServiceImpl")
    @Autowired
    private INFTTokenService nftTokenService;

    @PostMapping// creamos
    public ResponseEntity<NFTResponse> createNFT(@Valid @RequestBody NFTCreateRequest request) {
        NFTToken createdNFT = nftTokenService.createNFT(request);
        NFTResponse response = nftTokenService.toResponse(createdNFT);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}") //actualizamos
    public ResponseEntity<NFTResponse> updateNFT(@PathVariable Long id, @RequestBody NFTCreateRequest request) {
        NFTToken updatedNFT = nftTokenService.updateNFT(id, request);
        NFTResponse response = ((NFTTokenServiceImpl) nftTokenService).toResponse(updatedNFT);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}") // show
    public ResponseEntity<NFTResponse> getNFTById(@PathVariable Long id) {
        NFTResponse response = nftTokenService.getNFTById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping // index
    public ResponseEntity<List<NFTResponse>> getAllNFTs() {
        List<NFTToken> nfts = nftTokenService.getAllNFTs();
        List<NFTResponse> responses = nfts.stream()
                .map(nftTokenService::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}") // delete (por id)
    public ResponseEntity<MessageResponseDto> deleteNFT(@PathVariable Long id) {
        nftTokenService.deleteNFT(id);
        MessageResponseDto message = new MessageResponseDto("NFT con ID " + id + " ha sido eliminado.");
        return ResponseEntity.ok(message); // Retorna el mensaje con el estado HTTP 200 OK
    }

}




