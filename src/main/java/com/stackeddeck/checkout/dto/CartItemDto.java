package com.stackeddeck.checkout.dto;

import com.stackeddeck.catalog.dto.ProductDto;
import java.util.UUID;

public record CartItemDto(
        UUID id,
        ProductDto product,
        int quantity,
        Long priceAtAdd,
        Long subtotal,
        boolean savedForLater
) {}
