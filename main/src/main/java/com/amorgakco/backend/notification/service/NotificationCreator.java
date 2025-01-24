package com.amorgakco.backend.notification.service;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationType;
import com.amorgakco.backend.notification.domain.SendingType;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationCreator {

    public static NotificationRequest participationRequest(final Member sender,
        final Member receiver, Group group) {
        return NotificationRequest.builder()
            .sender(sender)
            .receiver(receiver)
            .group(group)
            .sendingType(SendingType.SMS_AND_WEB_PUSH)
            .title(NotificationType.PARTICIPATION_REQUEST.getTitle())
            .content(NotificationType.PARTICIPATION_REQUEST.getContent()
                .formatted(sender.getNickname(), group.getName()))
            .notificationType(NotificationType.PARTICIPATION_REQUEST)
            .build();
    }

    public static NotificationRequest participationApprove(final Member sender,
        final Member receiver, Group group) {
        return NotificationRequest.builder()
            .sender(sender)
            .receiver(receiver)
            .group(group)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_APPROVED.getTitle())
            .content(NotificationType.PARTICIPATION_APPROVED.getContent()
                .formatted(group.getName()))
            .notificationType(NotificationType.PARTICIPATION_APPROVED)
            .build();
    }

    public static NotificationRequest participationReject(final Member sender,
        final Member receiver, Group group) {
        return NotificationRequest.builder()
            .sender(sender)
            .receiver(receiver)
            .group(group)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_REJECTED.getTitle())
            .content(NotificationType.PARTICIPATION_REJECTED.getContent()
                .formatted(group.getName()))
            .notificationType(NotificationType.PARTICIPATION_REJECTED)
            .build();
    }

    public static NotificationRequest tardy(final GroupParticipant groupParticipant,
        final int requestMinute,final Group group) {
//        final Group group = groupParticipant.getGroup();
        final Member sender = groupParticipant.getMember();
        return NotificationRequest.builder()
            .sender(groupParticipant.getMember())
            .receiver(group.getHost())
            .group(group)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_TARDINESS.getTitle())
            .content(NotificationType.PARTICIPATION_TARDINESS.getContent()
                .formatted(sender.getNickname(), group.getName(), requestMinute))
            .notificationType(NotificationType.PARTICIPATION_TARDINESS)
            .build();
    }

    public static NotificationRequest withdraw(
        final GroupParticipant groupParticipant) {
        final Member sender = groupParticipant.getMember();
        final Group group = groupParticipant.getGroup();
        final Member receiver = group.getHost();
        return NotificationRequest.builder()
            .sender(sender)
            .receiver(receiver)
            .group(group)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.PARTICIPATION_WITHDRAW.getTitle())
            .content(NotificationType.PARTICIPATION_WITHDRAW.getContent()
                .formatted(sender.getNickname(), group.getName()))
            .notificationType(NotificationType.PARTICIPATION_WITHDRAW)
            .build();
    }

    public static NotificationRequest deleteGroup(
        final Member sender, final Member receiver, final Group group) {
        return NotificationRequest.builder()
            .sender(sender)
            .receiver(receiver)
            .group(group)
            .sendingType(SendingType.WEB_PUSH)
            .title(NotificationType.GROUP_DELETED.getTitle())
            .content(NotificationType.GROUP_DELETED.getContent()
                .formatted(sender.getNickname(), group.getName()))
            .notificationType(NotificationType.GROUP_DELETED)
            .build();
    }
}
