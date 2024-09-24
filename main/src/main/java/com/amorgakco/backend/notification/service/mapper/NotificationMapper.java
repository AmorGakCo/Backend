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
                .elementSize(notificationSlice.getContent().size())
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
                .title(notification.getTitle())
                .content(notification.getContent())
                .build();
    }

    public Notification toNotification(final NotificationRequest request) {
        return Notification.builder()
                .title(request.title())
                .sendingType(request.sendingType())
                .receiver(request.receiver())
                .content(request.content())
                .build();
    }
}
