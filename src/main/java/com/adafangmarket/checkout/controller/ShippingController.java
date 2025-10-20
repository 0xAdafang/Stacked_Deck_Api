package com.adafangmarket.checkout.controller;

import com.adafangmarket.checkout.Shipment;
import com.adafangmarket.checkout.enums.ShipmentStatus;
import com.adafangmarket.checkout.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {
    private final ShipmentService shipmentService;

    @PostMapping
    public Shipment createShipment(
            @RequestParam UUID orderId,
            @RequestParam String adressLine1,
            @RequestParam String city,
            @RequestParam String postalCode,
            @RequestParam String country
    ) {
        return shipmentService.createShipment(orderId, adressLine1, city, postalCode, country);
    }

    @PutMapping("/{shipmentId}/status")
    public void updateShipmentStatus(
            @PathVariable UUID shipmentId,
            @RequestParam ShipmentStatus status,
            @RequestParam(required = false)
            String trackingNumber) {
        shipmentService.updateShipmentStatus(shipmentId, status, trackingNumber);
    }

}
