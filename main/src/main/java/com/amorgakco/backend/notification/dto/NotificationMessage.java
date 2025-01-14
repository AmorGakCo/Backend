package com.amorgakco.backend.notification.dto;

import com.amorgakco.backend.notification.domain.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationMessage(
    String notificationId,
    String title,
    String content,
    Long groupId,
    Long senderMemberId,
    Long receiverMemberId,
    NotificationType notificationType,
    LocalDateTime createdAt) {

}
