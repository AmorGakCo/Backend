package com.amorgakco.backend.notification.infrastructure.consumer;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationTitle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationCreator {

    public static NotificationRequest participationNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.PARTICIPATION_REQUEST)
                .content(sender.getNickname() + "님께서 모각코에 참여하길 원합니다.")
                .build();
    }

    public static NotificationRequest participationApproveNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.PARTICIPATION_APPROVED)
                .content(
                        sender.getNickname() + "님께서" + receiver.getNickname() + "의 모각코 참여를 허가했습니다.")
                .build();
    }

    public static NotificationRequest participationRejectNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.PARTICIPATION_REJECTED)
                .content(
                        sender.getNickname() + "님께서" + receiver.getNickname() + "의 모각코 참여를 거절했습니다.")
                .build();
    }
}
