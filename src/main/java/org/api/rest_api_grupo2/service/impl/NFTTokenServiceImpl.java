package org.api.rest_api_grupo2.service.impl;

import org.api.rest_api_grupo2.dto.request.NFTCreateRequest;
import org.api.rest_api_grupo2.enums.ArtType;
import org.api.rest_api_grupo2.exceptions.NotFoundException;
import org.api.rest_api_grupo2.model.ImageUrl;
import org.api.rest_api_grupo2.model.User;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.repository.ImageUrlRepository;
import org.api.rest_api_grupo2.repository.NFTTokenRepository;
import org.api.rest_api_grupo2.dto.response.NFTResponse;
import org.api.rest_api_grupo2.service.INFTTokenService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NFTTokenServiceImpl implements INFTTokenService {
    private final NFTTokenRepository nftTokenRepository;
    private final ImageUrlRepository imageUrlRepository;

    public NFTTokenServiceImpl(NFTTokenRepository nftTokenRepository, ImageUrlRepository imageUrlRepository) {
        this.nftTokenRepository = nftTokenRepository;
        this.imageUrlRepository = imageUrlRepository;
    }

    @Override
    public NFTToken createNFT(NFTCreateRequest request) {
        NFTToken nft = new NFTToken();
        nft.setTitle(request.getTitle());
        nft.setDescription(request.getDescription());
        nft.setPrice(request.getPrice());

        ArtType tipo;
        try {
            tipo = ArtType.valueOf(request.getArtType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Tipo de arte inválido: " + request.getArtType());
        }
        nft.setArtType(tipo);
        nft.setPhysicalPieces(request.getPhysicalPieces());
        nft.setAvailable(request.getAvailable());
        nft.setReleaseDate(LocalDateTime.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        nft.setUser(user);

        NFTToken savedNFT = nftTokenRepository.save(nft);

        List<ImageUrl> images = request.getImageUrls().stream()
                .map(url -> {
                    ImageUrl img = new ImageUrl();
                    img.setUrl(url);
                    img.setNftToken(savedNFT);
                    return imageUrlRepository.save(img);
                }).collect(Collectors.toList());
        savedNFT.setImageUrls(images);
        return nftTokenRepository.save(savedNFT);
    }

    //Actualizamos el Token
    @Override
    public NFTToken updateNFT(Long id, NFTCreateRequest request) {
        NFTToken existingNFT = nftTokenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NFT no encontrado con id: " + id));

        existingNFT.setTitle(request.getTitle());
        existingNFT.setDescription(request.getDescription());
        existingNFT.setPrice(request.getPrice());
        existingNFT.setArtType(ArtType.valueOf(request.getArtType().toUpperCase()));
        existingNFT.setPhysicalPieces(request.getPhysicalPieces());
        existingNFT.setAvailable(request.getAvailable());


        List<ImageUrl> newImageUrls = request.getImageUrls().stream().map(url -> {
            ImageUrl image = new ImageUrl();
            image.setUrl(url);
            image.setNftToken(existingNFT);
            return image;
        }).collect(Collectors.toList());

        existingNFT.getImageUrls().clear();
        existingNFT.getImageUrls().addAll(newImageUrls);

        return nftTokenRepository.save(existingNFT);
    }
    // hacemos el show
    @Override
    public NFTResponse getNFTById(Long id) {
        NFTToken nft = nftTokenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NFT no encontrado con id: " + id));
        return toResponse(nft);
    }

    //Index - retorna todos los nfts
    @Override
    public List<NFTToken> getAllNFTs() {
        return nftTokenRepository.findAll();
    }
    // Delete - por ID
    @Override
    public void deleteNFT(Long id) {
        NFTToken nft = nftTokenRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("NFT no encontrado con id: " + id));

        nft.setAvailable(false);
        nftTokenRepository.save(nft);
    }

    public NFTResponse toResponse(NFTToken nft) {
        List<String> imageUrls = nft.getImageUrls().stream()
                .map(ImageUrl::getUrl)
                .collect(Collectors.toList());

        return new NFTResponse(
                nft.getId(),
                nft.getTitle(),
                nft.getDescription(),
                nft.getPrice(),
                nft.getArtType().toString(),
                nft.getPhysicalPieces(),
                nft.getAvailable(),
                imageUrls
        );
    }

}



