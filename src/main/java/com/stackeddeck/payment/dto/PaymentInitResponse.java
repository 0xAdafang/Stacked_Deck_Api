package com.stackeddeck.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInitResponse {
    private String paymentUrl;
}
