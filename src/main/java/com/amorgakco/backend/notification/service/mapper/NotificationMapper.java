package com.amorgakco.backend.notification.service.mapper;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.NotificationRequest;

import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toNotification(final NotificationRequest request) {
        return Notification.builder()
                .receiver(request.receiver())
                .sender(request.sender())
                .notificationTitle(request.notificationTitle())
                .content(request.content())
                .build();
    }
}
