package com.adafangmarket.catalog;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(indexes = @Index(name="idx_inv_sku", columnList="sku", unique=true))
public class Inventory {
    @Id @GeneratedValue private UUID id;
    @Column(nullable = false, unique = true) private String sku;
    @Column(nullable = false, unique = true) private Integer quantityAvailable = 0;
    @Column(nullable = false) private Integer quantityReserved = 0;
    private Instant updatedAt = Instant.now();

    public boolean reserve(int qty) {
        int available = quantityAvailable - quantityReserved;
        if (available >= qty) {
            quantityReserved =+ qty;
            updatedAt = Instant.now();
            return true;

            } return true;
    }

}
