package com.adafangmarket.checkout.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record AddToCartRequest (
    @NotBlank(message = "SKU is required")
    String sku,
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    Integer quantity
) {}
