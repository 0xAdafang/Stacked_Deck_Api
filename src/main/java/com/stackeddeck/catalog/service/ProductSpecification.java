package com.stackeddeck.catalog.service;

import com.stackeddeck.catalog.Product;
import com.stackeddeck.catalog.enums.CardCondition;
import com.stackeddeck.catalog.enums.ProductType;
import com.stackeddeck.catalog.enums.Rarity;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class ProductSpecification {

    public static Specification<Product> withFilters(
            String q, ProductType type, UUID categoryId,
            Long minPrice, Long maxPrice, // <-- Nouveaux filtres
            Rarity rarity, CardCondition condition, Boolean inStock) {

        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (q != null && !q.isBlank()) {
                String likePattern = "%" + q.toLowerCase() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("name")), likePattern),
                        cb.like(cb.lower(root.get("sku")), likePattern)
                ));
            }

            if (type != null) {
                predicate = cb.and(predicate, cb.equal(root.get("type"), type));
            }

            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price").get("baseAmount"), minPrice));
            }
            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price").get("baseAmount"), maxPrice));
            }
            // ----------------------------------------

            if (categoryId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
            }

            if (rarity != null) {
                predicate = cb.and(predicate, cb.equal(root.get("rarity"), rarity));
            }

            if (inStock != null && inStock) {
                predicate = cb.and(predicate, cb.isTrue(root.get("active")));
            }

            return predicate;
        };
    }
}