package com.stackeddeck.catalog.dto;

import com.stackeddeck.catalog.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import java.time.Instant;
import java.util.List;

public record ProductCreateRequest (
        @NotBlank String sku,
        @NotBlank String name,
        @NotBlank String slug,
        String description,
        List<String> images,
        @NotNull ProductType type,
        @NotNull Long baseAmount,
        @NotBlank String currency,
        Long promoAmount,
        Instant promoStart,
        Instant promoEnd,
        UUID categoryId,
        Boolean active
) {}
