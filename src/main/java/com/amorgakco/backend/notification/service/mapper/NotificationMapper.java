package com.amorgakco.backend.notification.service.mapper;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.NotificationMessage;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationMapper {

    public NotificationMessageResponse toNotificationMessageResponse(
            final Slice<Notification> notificationSlice) {
        return NotificationMessageResponse.builder()
                .page(notificationSlice.getNumber())
                .elementSize(notificationSlice.getSize())
                .hasNext(notificationSlice.hasNext())
                .notificationMessages(getNotificationMessages(notificationSlice.getContent()))
                .build();
    }

    private List<NotificationMessage> getNotificationMessages(
            final List<Notification> notifications) {
        return notifications.stream().map(this::toNotificationMessage).toList();
    }

    private NotificationMessage toNotificationMessage(final Notification notification) {
        return NotificationMessage.builder()
                .title(notification.getNotificationTitle().getTitle())
                .content(notification.getContent())
                .build();
    }

    public Notification toNotification(final NotificationRequest request) {
        return Notification.builder()
                .receiver(request.receiver())
                .sender(request.sender())
                .notificationTitle(request.notificationTitle())
                .content(request.content())
                .build();
    }
}
