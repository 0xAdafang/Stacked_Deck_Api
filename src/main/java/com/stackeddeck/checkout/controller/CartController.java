package com.stackeddeck.checkout.controller;


import com.stackeddeck.checkout.dto.AddToCartRequest;
import com.stackeddeck.checkout.dto.CartDto;
import com.stackeddeck.checkout.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto getCart(@RequestParam UUID userId) {
        return cartService.getMyCart(userId);
    }

    @PostMapping("/add")
    public void addItem(@RequestParam UUID userId, @RequestBody AddToCartRequest request) {
        cartService.addItem(userId, request);
    }


    @PutMapping("/item/{itemId}")
    public void updateItemQuantity(@RequestParam UUID userId, @PathVariable UUID itemId, @RequestParam int quantity) {
        cartService.updateQuantity(userId, itemId, quantity);
    }

    @PutMapping("/item/{itemId}/toggle-save")
    public void toggleSaved(@RequestParam UUID userId, @PathVariable UUID itemId) {
        cartService.toggleSavedForLater(userId, itemId);
    }


    @DeleteMapping("/item/{itemId}")
    public void removeItem(@RequestParam UUID userId, @PathVariable UUID itemId) {
        cartService.removeItem(userId, itemId);
    }


    @DeleteMapping
    public void clearCart(@RequestParam UUID userId) {
        cartService.clearCart(userId);
    }

}
