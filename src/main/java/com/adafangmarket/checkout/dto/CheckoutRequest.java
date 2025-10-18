package com.adafangmarket.checkout.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CheckoutRequest (
         @NotNull(message = "User ID is required")
         UUID userId

) {}
