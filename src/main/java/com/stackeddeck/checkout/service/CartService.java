package com.stackeddeck.checkout.service;


import com.stackeddeck.catalog.Inventory;
import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.dto.ProductDto;
import com.stackeddeck.catalog.repo.InventoryRepository;
import com.stackeddeck.catalog.repo.ProductRepository;
import com.stackeddeck.checkout.Cart;
import com.stackeddeck.checkout.CartItem;
import com.stackeddeck.checkout.dto.AddToCartRequest;
import com.stackeddeck.checkout.dto.CartDto;
import com.stackeddeck.checkout.mapper.CartMapper;
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
    private final CartMapper cartMapper;

    @Transactional
    public CartDto getMyCart(UUID userId) {
        Cart cart = getOrCreateCartEntity(userId);
        return cartMapper.toDto(cart);
    }

    private Cart getOrCreateCartEntity(UUID userId) {
        return carts.findByUserId(userId).orElseGet(() ->  {
            Cart newCart = Cart.builder().userId(userId).build();
            return carts.save(newCart);
        });
    }

    @Transactional
    public void addItem(UUID userId, AddToCartRequest request) {
        Cart cart = getOrCreateCartEntity(userId);
        Product product = products.findBySku(request.sku())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));



        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getSku().equals(request.sku()) && !item.isSavedForLater())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());

        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .sku(request.sku())
                    .quantity(request.quantity())
                    .priceAtAdd(product.getPrice().getEffectiveAmount())
                    .product(product)
                    .savedForLater(false)
                    .build();
            cart.getItems().add(item);
        }
        carts.save(cart);
    }

    @Transactional
    public void updateQuantity(UUID userId, UUID itemId, int quantity) {
        Cart cart = getOrCreateCartEntity(userId);
        CartItem item = findItem(cart, itemId);

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }
        carts.save(cart);
    }

    @Transactional
    public void toggleSavedForLater(UUID userId, UUID itemId) {
        Cart cart = getOrCreateCartEntity(userId);
        CartItem item = findItem(cart, itemId);
        item.setSavedForLater(!item.isSavedForLater());
        carts.save(cart);
    }

    @Transactional
    public void removeItem(UUID userId, UUID itemId) {
        Cart cart = getOrCreateCartEntity(userId);
        CartItem item = findItem(cart, itemId);
        cart.getItems().remove(item);
        carts.save(cart);
    }

    @Transactional
    public void clearCart(UUID userId) {
        Cart cart = getOrCreateCartEntity(userId);
        cart.getItems().clear();
        carts.save(cart);
    }

    private CartItem findItem(Cart cart, UUID itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
    }



}
