package com.adafangmarket.catalog.dto;

import com.adafangmarket.catalog.enums.ProductType;
import com.adafangmarket.pricing.Price;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.UUID;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDto(
        UUID id, String sku, String name, String slug, String description,
        List<String> images, ProductType type, Long price, String currency, Long promoAmount,
        UUID categoryId, boolean active
) {
    public ProductDto(UUID id, String sku, String name, String slug, String description,
                      List<String> images, ProductType type, Price price,
                      UUID categoryId, boolean active) {
        this(id, sku, name, slug, description, images, type,
                price.getEffectiveAmount(), price.getCurrency(), price.getPromoAmount(),
                categoryId, active);
    }
}
