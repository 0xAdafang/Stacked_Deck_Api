package com.stackeddeck.admin.controller;

import com.stackeddeck.checkout.Order;
import com.stackeddeck.checkout.enums.OrderStatus;
import com.stackeddeck.checkout.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderRepository orderRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(statusStr.toUpperCase());
            order.setStatus(newStatus);
            return ResponseEntity.ok(orderRepository.save(order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}