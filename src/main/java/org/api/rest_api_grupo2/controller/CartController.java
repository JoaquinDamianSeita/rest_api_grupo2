package org.api.rest_api_grupo2.controller;

import java.util.List;

import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.NFTToken;
import org.api.rest_api_grupo2.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<Cart> createCartIfNotExists(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.createCartIfNotExists(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add/{nftTokenId}")
    public ResponseEntity<Cart> addNFTToCart(@PathVariable Long userId, @PathVariable Long nftTokenId){
        return ResponseEntity.ok(cartService.addNFTToken(userId, nftTokenId));
    }

    @DeleteMapping("/{userId}/remove/{nftTokeId}")
    public ResponseEntity<Cart> removeNFTFromCart(@PathVariable Long userId, @PathVariable LOng nftTokenId){
        return ResponseEntity.ok(cartService.removeNFTFromCart(userId, userId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/tokens")
    public ResponseEntity<List<NFTToken>> listCartTokens(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.listCartTokens(userId));
    }
    
}
