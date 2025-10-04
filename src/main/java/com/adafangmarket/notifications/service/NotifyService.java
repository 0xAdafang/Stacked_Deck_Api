package com.adafangmarket.notifications.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyService {
    private final SimpMessagingTemplate ws;

    public void broadcast(String message) {
        ws.convertAndSend("/topic/notifcations", message);
    }

}
