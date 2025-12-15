package com.stackeddeck.checkout.dto;

public record OrderItemDto(
        String sku,
        int quantity,
        Long price
) {}
