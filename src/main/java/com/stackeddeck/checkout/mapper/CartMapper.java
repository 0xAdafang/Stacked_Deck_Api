package com.stackeddeck.checkout.mapper;


import com.stackeddeck.catalog.mapper.CatalogMapper;
import com.stackeddeck.checkout.Cart;
import com.stackeddeck.checkout.CartItem;
import com.stackeddeck.checkout.dto.CartDto;
import com.stackeddeck.checkout.dto.CartItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CatalogMapper productMapper;

    public CartDto toDto(Cart cart) {
        var items = cart.getItems().stream()
                .map(this::toItemDto)
                .toList();

        long total = items.stream()
                .filter(i -> !i.savedForLater())
                .mapToLong(CartItemDto::subtotal)
                .sum();

        int count = items.stream()
                .filter (i -> !i.savedForLater())
                .mapToInt(CartItemDto::quantity)
                .sum();

        return new CartDto(cart.getId(), items, total, count);
    }

    private CartItemDto toItemDto(CartItem item) {
        long currentPrice = item.getProduct().getPrice().getEffectiveAmount();
        return new CartItemDto(
                item.getId(),
                productMapper.toDto(item.getProduct()),
                item.getQuantity(),
                item.getPriceAtAdd(),
                currentPrice * item.getQuantity(),
                item.isSavedForLater()

        );
    }
}
