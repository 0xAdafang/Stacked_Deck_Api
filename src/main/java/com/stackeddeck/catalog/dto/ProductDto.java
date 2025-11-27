package com.stackeddeck.catalog.dto;

import com.stackeddeck.catalog.enums.ProductType;
import com.stackeddeck.catalog.enums.Rarity;
import com.stackeddeck.pricing.Price;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDto(
        UUID id,
        String sku,
        String name,
        String slug,
        String description,
        String image,
        List<String> images,
        ProductType type,
        Long price,
        String currency,
        Long promoAmount,
        UUID categoryId,
        String categoryName,
        Rarity rarity,
        boolean active
) {

    public ProductDto(UUID id, String sku, String name, String slug, String description,
                      String image, List<String> images, ProductType type, Price price,
                      UUID categoryId, String categoryName, Rarity rarity, boolean active) {
        this(id, sku, name, slug, description, image, images, type,
                price != null ? price.getEffectiveAmount() : null,
                price != null ? price.getCurrency() : null,
                price != null ? price.getPromoAmount() : null,
                categoryId, categoryName, rarity, active);
    }
}