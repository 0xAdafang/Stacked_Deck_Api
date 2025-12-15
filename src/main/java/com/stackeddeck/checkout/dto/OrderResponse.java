package com.stackeddeck.checkout.dto;

import com.stackeddeck.checkout.enums.OrderStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        Instant date,
        Long totalAmount,
        OrderStatus status,
        List<OrderItemDto> items
) {}
