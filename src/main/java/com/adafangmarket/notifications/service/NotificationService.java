package com.adafangmarket.notifications.service;

import com.adafangmarket.notifications.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String userId, String message, String orderId, String shipmentId) {
        NotificationMessage notification = new NotificationMessage(userId, message, orderId, shipmentId);
        messagingTemplate.convertAndSend("/topic/user/" + userId, notification);
    }

}
