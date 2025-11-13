package com.stackeddeck.notifications.dto;

public record NotificationMessage(
        String userId,
        String message,
        String orderId,
        String shipmentId
) {}
