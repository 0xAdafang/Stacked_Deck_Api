package com.stackeddeck.payment.dto;

import lombok.Data;

@Data
public class PaymentInitRequest {
    private Long amount;
    private String currency;
    private String email;
}
