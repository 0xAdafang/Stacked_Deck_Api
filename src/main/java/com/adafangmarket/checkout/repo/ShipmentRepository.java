package com.adafangmarket.checkout.repo;

import com.adafangmarket.checkout.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
}
