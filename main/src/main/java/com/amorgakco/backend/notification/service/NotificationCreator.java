package com.amorgakco.backend.notification.service;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationType;
import com.amorgakco.backend.notification.domain.SendingType;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationCreator {

    public static NotificationRequest participationRequest(
        final Member sender, final Member receiver, String groupName) {
        return NotificationRequest.builder()
            .receiver(receiver)
            .sendingType(SendingType.SMS_AND_WEB_PUSH)
            .title(NotificationType.PARTICIPATION_REQUEST.getTitle())
            .content(NotificationType.PARTICIPATION_REQUEST.getContent()
                .formatted(sender.getNickname(), groupName))
            .build();
    }

    public static NotificationRequest participationApprove(
        final Member receiver, final String groupName) {
        return NotificationRequest.builder()
            .receiver(receiver)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_APPROVED.getTitle())
            .content(NotificationType.PARTICIPATION_APPROVED.getContent()
                .formatted(groupName))
            .build();
    }

    public static NotificationRequest participationReject(
        final Member receiver, final String groupName) {
        return NotificationRequest.builder()
            .receiver(receiver)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_REJECTED.getTitle())
            .content(NotificationType.PARTICIPATION_REJECTED.getContent()
                .formatted(groupName))
            .build();
    }

    public static NotificationRequest tardy(
        final Member sender, final Member receiver, final String groupName,
        final int requestMinute) {
        return NotificationRequest.builder()
            .receiver(receiver)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_TARDINESS.getTitle())
            .content(NotificationType.PARTICIPATION_TARDINESS.getContent()
                .formatted(sender.getNickname(), groupName, requestMinute))
            .build();
    }

    public static NotificationRequest withdraw(
        final Member sender, final Member receiver, final String groupName) {
        return NotificationRequest.builder()
            .receiver(receiver)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_WITHDRAW.getTitle())
            .content(NotificationType.PARTICIPATION_WITHDRAW.getContent()
                .formatted(sender.getNickname(), groupName))
            .build();
    }
}
