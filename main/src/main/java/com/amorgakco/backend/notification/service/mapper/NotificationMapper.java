package com.amorgakco.backend.notification.service.mapper;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.NotificationMessage;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

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
            .notificationId(notification.getId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .notificationType(notification.getNotificationType())
            .senderMemberId(notification.getSender().getId())
            .receiverMemberId(notification.getReceiver().getId())
            .groupId(notification.getGroup().getId())
            .build();
    }

    public Notification toNotification(final NotificationRequest request) {
        return Notification.builder()
            .title(request.title())
            .sendingType(request.sendingType())
            .receiver(request.receiver())
            .sender(request.sender())
            .group(request.group())
            .content(request.content())
            .notificationType(request.notificationType())
            .build();
    }
}
