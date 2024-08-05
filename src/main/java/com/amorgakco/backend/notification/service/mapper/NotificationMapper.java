package com.amorgakco.backend.notification.service.mapper;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.domain.NotificationType;
import com.amorgakco.backend.notification.dto.NotificationRequest;

import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toSmsNotification(final NotificationRequest request) {
        return Notification.builder()
                .receiver(request.receiver())
                .sender(request.sender())
                .notificationType(NotificationType.SMS)
                .notificationTitle(request.notificationTitle())
                .content(request.content())
                .build();
    }

    public Notification toFcmNotification(final NotificationRequest request) {
        return Notification.builder()
                .receiver(request.receiver())
                .sender(request.sender())
                .notificationType(NotificationType.FCM)
                .notificationTitle(request.notificationTitle())
                .content(request.content())
                .build();
    }
}
