package com.stackeddeck.checkout.service;

import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.repo.ProductRepository;
import com.stackeddeck.checkout.Cart;
import com.stackeddeck.checkout.CartItem;
import com.stackeddeck.checkout.PromoCode;
import com.stackeddeck.checkout.dto.AddToCartRequest;
import com.stackeddeck.checkout.dto.CartDto;
import com.stackeddeck.checkout.mapper.CartMapper;
import com.stackeddeck.checkout.repo.CartRepository;
import com.stackeddeck.checkout.repo.PromoCodeRepository;
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
    private final CartMapper cartMapper;
    private final PromoCodeRepository promoRepo;


    @Transactional
    public CartDto getMyCart(UUID userId) {
        Cart cart = getOrCreateCartEntity(userId);
        PromoCode promo = null;
        if (cart.getPromoCode() != null) {
            promo = promoRepo.findByCode(cart.getPromoCode()).orElse(null);
        }
        return cartMapper.toDto(cart, promo);
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


        int availableStock = product.getStockQuantity();


        int currentCartQty = cart.getItems().stream()
                .filter(i -> i.getSku().equals(request.sku()))
                .mapToInt(CartItem::getQuantity)
                .sum();

        if (currentCartQty + request.quantity() > availableStock) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock available");
        }


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
            // Petite sécurité en plus
            if (quantity > item.getProduct().getStockQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Max quantity reached");
            }
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

    @Transactional
    public void applyPromo(UUID userId, String code) {
        Cart cart = getOrCreateCartEntity(userId);

        if (code == null || code.isBlank()) {
            cart.setPromoCode(null);
        } else {
            PromoCode pc = promoRepo.findByCode(code)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code invalide"));

            if (!pc.isValid()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expiré");
            }
            cart.setPromoCode(code.toUpperCase());
        }
        carts.save(cart);
    }

    private CartItem findItem(Cart cart, UUID itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found"));
    }
}