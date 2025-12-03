package com.stackeddeck.checkout.controller;

import com.stackeddeck.checkout.dto.AddToCartRequest;
import com.stackeddeck.checkout.dto.CartDto;
import com.stackeddeck.checkout.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartDto getCart() {
        return cartService.getMyCart(getCurrentUserId());
    }

    @PostMapping("/add")
    public void addItem(@RequestBody AddToCartRequest request) {
        cartService.addItem(getCurrentUserId(), request);
    }

    @PostMapping("/promo")
    public void applyPromoCode(@RequestParam String code) {
        cartService.applyPromo(getCurrentUserId(), code);
    }

    @PutMapping("/item/{itemId}")
    public void updateItemQuantity(@PathVariable UUID itemId, @RequestParam int quantity) {
        cartService.updateQuantity(getCurrentUserId(), itemId, quantity);
    }

    @PutMapping("/item/{itemId}/toggle-save")
    public void toggleSaved(@PathVariable UUID itemId) {
        cartService.toggleSavedForLater(getCurrentUserId(), itemId);
    }

    @DeleteMapping("/item/{itemId}")
    public void removeItem(@PathVariable UUID itemId) {
        cartService.removeItem(getCurrentUserId(), itemId);
    }

    @DeleteMapping
    public void clearCart() {
        cartService.clearCart(getCurrentUserId());
    }


    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        try {

            return UUID.fromString((String) auth.getPrincipal());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid User ID in token");
        }
    }
}
