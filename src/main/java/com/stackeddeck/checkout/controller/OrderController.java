package com.stackeddeck.checkout.controller;

import com.stackeddeck.checkout.dto.OrderResponse;
import com.stackeddeck.checkout.service.OrderService;
import com.stackeddeck.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getMyOrders() {
        return orderService.getUserOrders(SecurityUtils.getCurrentUserId());
    }
}
