package com.stackeddeck.checkout.dto;

public record CheckoutSummaryDto (
    long subtotal,
    long discount,
    long shippingCost,
    long taxAmount,
    long total,
    String shippingMethodLabel
) {}
