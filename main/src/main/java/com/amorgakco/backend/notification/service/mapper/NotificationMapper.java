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
            .senderMemberId(notification.getSenderId())
            .receiverMemberId(notification.getReceiverId())
            .createdAt(notification.getCreatedAt())
            .build();
    }

    public Notification toNotificationCache(final NotificationRequest request) {
        return Notification.builder()
            .title(request.title())
            .sendingType(request.sendingType())
            .receiverId(request.receiver().getId())
            .senderId(request.sender().getId())
            .content(request.content())
            .phoneNumber(request.receiver().getPhoneNumber())
            .isSmsNotificationActivated(request.receiver().isSmsNotificationActivated())
            .notificationType(request.notificationType())
            .build();
    }
}
