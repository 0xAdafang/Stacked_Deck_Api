package com.stackeddeck.checkout.controller;

import com.stackeddeck.checkout.Order;
import com.stackeddeck.checkout.dto.CheckoutRequest;
import com.stackeddeck.checkout.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final OrderService orderService;

    @PostMapping
    public UUID createOrder(@RequestBody CheckoutRequest request) {
        Order order = orderService.createOrder(request);
        return order.getId();
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestParam String paymentIntentId, @RequestParam String status)  {
        orderService.handlePaymentWebhook(paymentIntentId, status);
    }
}
