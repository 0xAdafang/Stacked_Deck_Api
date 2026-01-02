package com.stackeddeck.payment.controller;

import com.stackeddeck.payment.dto.PaymentInitRequest;
import com.stackeddeck.payment.dto.PaymentInitResponse;
import com.stackeddeck.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentInitResponse> checkout(@RequestBody PaymentInitRequest request) {
        return ResponseEntity.ok(paymentService.createCheckoutSession(request));
    }
}
