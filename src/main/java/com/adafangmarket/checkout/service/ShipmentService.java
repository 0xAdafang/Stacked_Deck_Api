package com.adafangmarket.checkout.service;

import com.adafangmarket.checkout.Order;
import com.adafangmarket.checkout.Shipment;
import com.adafangmarket.checkout.enums.OrderStatus;
import com.adafangmarket.checkout.enums.ShipmentStatus;
import com.adafangmarket.checkout.repo.OrderRepository;
import com.adafangmarket.checkout.repo.ShipmentRepository;
import com.adafangmarket.notifications.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {
    private final ShipmentRepository shipments;
    private final OrderRepository orders;
    private final NotificationService notificationService;

    @Transactional
    public Shipment createShipment(UUID orderId, String adressLine1, String city, String postalCode, String country) {
        Order order = orders.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        if (!order.getStatus().equals(OrderStatus.PAID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order status must be paid before any shipping");

        }
        Shipment shipment = Shipment.builder()
                .order(order)
                .adressLine1(adressLine1)
                .city(city)
                .PostalCode(postalCode)
                .country(country)
                .status(ShipmentStatus.PENDING)
                .build();
        return shipments.save(shipment);
    }

    @Transactional
    public void updateShipmentStatus(UUID shipmentId, ShipmentStatus status, String trackingNumber) {
        Shipment shipment = shipments.findById(shipmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));
        shipment.setStatus(status);
        shipment.setTrackingNumber(trackingNumber);
        shipments.save(shipment);

        notificationService.sendNotification(shipment.getOrder().getUserId().toString(), "Expedition status #" + shipment.getId() + " updated : " + status, shipment.getOrder().getId().toString(), shipment.getId().toString());
    }
}
