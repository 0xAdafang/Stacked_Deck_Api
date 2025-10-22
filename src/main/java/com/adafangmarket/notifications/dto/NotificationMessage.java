package com.adafangmarket.notifications.dto;

public record NotificationMessage(
        String userId,
        String message,
        String orderId,
        String shipmentId
) {}
