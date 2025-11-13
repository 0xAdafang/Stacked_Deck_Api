package com.stackeddeck.catalog;


import com.stackeddeck.catalog.enums.ProductType;
import com.stackeddeck.pricing.Price;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(indexes = {
        @Index(name="idx_prod_slug", columnList="slug", unique=true),
        @Index(name="idx_prod_sku", columnList="sku", unique=true)
})
public class Product {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false, unique = true) private String sku;
    @Column(nullable = false) private String name;
    @Column(nullable = false, unique = true) private String slug;
    @Column(columnDefinition = "TEXT") private String description;
    @ElementCollection private List<String> images = new ArrayList<>();
    @Enumerated(EnumType.STRING) private ProductType type;
    @Embedded private Price price;
    @ManyToOne(fetch = FetchType.LAZY) private Category category;
    private boolean active = true;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    @PrePersist protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate protected void onUpdate() {
        updatedAt = Instant.now();
    }



}
