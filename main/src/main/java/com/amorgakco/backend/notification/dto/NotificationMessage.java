package com.amorgakco.backend.notification.dto;

import com.amorgakco.backend.notification.domain.NotificationType;
import lombok.Builder;

@Builder
public record NotificationMessage(
    Long notificationId,
    String title,
    String content,
    Long groupId,
    Long senderMemberId,
    Long receiverMemberId,
    NotificationType notificationType) {

}
