package com.stackeddeck.checkout.dto;


import java.util.List;
import java.util.UUID;


public record CartDto (

        UUID id,
        List<CartItemDto> items,
        long subtotal,
        long discountAmount,
        long totalAmount,
        String appliedCode,
        int totalItems
) {}
