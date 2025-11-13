package com.stackeddeck.checkout.repo;

import com.stackeddeck.checkout.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
}
