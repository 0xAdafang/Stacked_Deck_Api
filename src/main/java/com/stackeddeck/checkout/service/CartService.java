package com.stackeddeck.checkout.service;


import com.stackeddeck.catalog.Inventory;
import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.repo.InventoryRepository;
import com.stackeddeck.catalog.repo.ProductRepository;
import com.stackeddeck.checkout.Cart;
import com.stackeddeck.checkout.CartItem;
import com.stackeddeck.checkout.dto.AddToCartRequest;
import com.stackeddeck.checkout.repo.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository carts;
    private final ProductRepository products;
    private final InventoryRepository inventory;

    @Transactional
    public Cart getOrCreateCart(UUID userId) {
        return carts.findByUserId(userId).orElseGet(() ->  {
            Cart newCart = Cart.builder().userId(userId).build();
            return carts.save(newCart);
        });
    }

    @Transactional
    public CartItem addItem(UUID userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);
        Product product = products.findBySku(request.sku())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        Inventory inv = inventory.findBySku(request.sku())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found"));

        if (!inv.reserve(request.quantity())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock");
        }

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getSku().equals(request.sku()))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            int newQty = existingItem.getQuantity() + request.quantity();
            if (!inv.reserve(newQty - existingItem.getQuantity())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for additional quantity");
            }
            existingItem.setQuantity(newQty);
            existingItem.setPriceAtAdd(product.getPrice().getEffectiveAmount());
            existingItem.setProduct(product);
            return existingItem;
        }

        CartItem item = CartItem.builder()
                .cart(cart)
                .sku(request.sku())
                .quantity(request.quantity())
                .priceAtAdd(product.getPrice().getEffectiveAmount())
                .build();
        cart.getItems().add(item);
        carts.save(cart);
        return item;
    }

    @Transactional
    public void removeItem(UUID userId, UUID itemId) {
        Cart cart = carts.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart"));
        cart.getItems().remove(item);
        carts.save(cart);
    }

    @Transactional
    public void clearCart(UUID userId) {
        Cart cart = carts.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
        cart.getItems().clear();
        carts.save(cart);
    }
}
