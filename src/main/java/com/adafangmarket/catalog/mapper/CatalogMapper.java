package com.adafangmarket.catalog.mapper;


import com.adafangmarket.catalog.Category;
import com.adafangmarket.catalog.Product;
import com.adafangmarket.catalog.dto.ProductCreateRequest;
import com.adafangmarket.catalog.dto.ProductDto;
import com.adafangmarket.pricing.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CatalogMapper {
    public ProductDto toDto(Product p) {
        return new ProductDto(p.getId(), p.getSku(), p.getName(), p.getSlug(), p.getDescription(),
                p.getImages(), p.getType(), p.getPrice(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.isActive()
        );

    }

    public void updateEntity(Product p, ProductCreateRequest r, Category cat) {
        p.setSku(r.sku());
        p.setName(r.name());
        p.setSlug(r.slug());
        p.setDescription(r.description());
        p.setImages(r.images()!=null ? new ArrayList<>(r.images()) : new ArrayList<>());
        p.setType(r.type());
        p.setPrice(Price.builder()
                .baseAmount(r.baseAmount())
                .currency(r.currency())
                .promoAmount(r.promoAmount())
                .promoStart(r.promoStart())
                .promoEnd(r.promoEnd())
                .build());
        p.setCategory(cat);
        if (r.active() !=null) p.setActive(r.active());
    }
}
