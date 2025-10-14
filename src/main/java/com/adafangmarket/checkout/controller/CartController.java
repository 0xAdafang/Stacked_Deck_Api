package com.adafangmarket.checkout.controller;


import com.adafangmarket.checkout.Cart;
import com.adafangmarket.checkout.dto.AddToCartRequest;
import com.adafangmarket.checkout.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Cart getCart(@RequestParam UUID userId) {
        return cartService.getOrCreateCart(userId);
    }

    @PostMapping("/add")
    public void addItem(@RequestParam UUID userId, @RequestBody AddToCartRequest request) {
        cartService.addItem(userId, request);
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
