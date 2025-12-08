package com.stackeddeck.checkout.controller;

import com.stackeddeck.checkout.Order;
import com.stackeddeck.checkout.dto.CheckoutRequest;
import com.stackeddeck.checkout.dto.CheckoutSummaryDto;
import com.stackeddeck.checkout.service.CheckoutService;
import com.stackeddeck.checkout.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.stackeddeck.security.SecurityUtils.getCurrentUserId;
import java.util.UUID;



@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final OrderService orderService;
    private final CheckoutService checkoutService;



    @PostMapping
    public UUID createOrder(@RequestBody CheckoutRequest request) {
        Order order = orderService.createOrder(request);
        return order.getId();
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestParam String paymentIntentId, @RequestParam String status)  {
        orderService.handlePaymentWebhook(paymentIntentId, status);
    }

    @GetMapping("/summary")
    public CheckoutSummaryDto getSummary(
            @RequestParam(defaultValue = "STANDARD") String shippingType,
            @RequestParam(defaultValue = "CA") String country) {


        return checkoutService.getCheckoutSummary(getCurrentUserId(), shippingType, country);
    }
}
