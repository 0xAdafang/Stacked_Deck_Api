package com.adafangmarket.catalog.repo;

import com.adafangmarket.catalog.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findBySku(String sku);
}
