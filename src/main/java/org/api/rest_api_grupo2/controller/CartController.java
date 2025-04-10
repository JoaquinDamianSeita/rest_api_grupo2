package org.api.rest_api_grupo2.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.api.rest_api_grupo2.dto.request.NFTCartItemRequest;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private ICartService cartService;

    @PostMapping("/items")
    public ResponseEntity<?> createCart(@RequestBody NFTCartItemRequest request) throws BadRequestException{
        return new ResponseEntity<>(cartService.createCart(request), HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable Long cartId){
        return new ResponseEntity<>(cartService.deleteCart(cartId), HttpStatus.OK);
    }    

    @DeleteMapping("/items/{nftTokenId}")
    public ResponseEntity<?> removeNFT(@PathVariable Long nftTokenId) throws BadRequestException{
        return new ResponseEntity<>(cartService.removeNFT(nftTokenId), HttpStatus.OK);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<NFTToken>> getItems(@PathVariable Long cartId) {
        return new ResponseEntity<>(cartService.getItems(cartId), HttpStatus.OK);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateCart(@PathVariable Long cartId, @RequestBody List<NFTCartItemRequest> items) throws BadRequestException {
        return new ResponseEntity<>(cartService.updateCart(cartId, items), HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutCart() throws BadRequestException {
        return new ResponseEntity<>(cartService.checkoutCart(), HttpStatus.OK);
    }
}
