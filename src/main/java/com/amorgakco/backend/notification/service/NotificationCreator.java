package com.amorgakco.backend.notification.service;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationTitle;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationCreator {

    private static final String PARTICIPATION = "%s 님께서 모각코에 참여하길 원합니다.";
    private static final String PARTICIPATION_APPROVE = "%s 님께서 %s 님의 모각코 참여를 허가했습니다.";
    private static final String PARTICIPATION_REJECT = "%s 님께서 %s 님의 모각코 참여를 거절했습니다.";
    private static final String PARTICIPATION_WITHDRAW = "%s 님께서 모각코를 탈퇴하셨습니다.";
    private static final String PARTICIPATION_TARDINESS = "%s 님께서 모각코에 %d분 지각 알림을 보냈습니다.";

    public static NotificationRequest participationNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.PARTICIPATION_REQUEST)
                .content(PARTICIPATION.formatted(sender.getNickname()))
                .build();
    }

    public static NotificationRequest participationApproveNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.PARTICIPATION_APPROVED)
                .content(
                        PARTICIPATION_APPROVE.formatted(
                                sender.getNickname(), receiver.getNickname()))
                .build();
    }

    public static NotificationRequest participationRejectNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.PARTICIPATION_REJECTED)
                .content(
                        PARTICIPATION_REJECT.formatted(
                                sender.getNickname(), receiver.getNickname()))
                .build();
    }

    public static NotificationRequest withdrawNotification(
            final Member participant, final Member host) {
        return NotificationRequest.builder()
                .sender(participant)
                .receiver(host)
                .notificationTitle(NotificationTitle.PARTICIPATION_WITHDRAW)
                .content(PARTICIPATION_WITHDRAW.formatted(participant.getNickname()))
                .build();
    }

    public static NotificationRequest tardinessNotification(
            final Member participant, final Member host, final Integer minute) {
        return NotificationRequest.builder()
                .sender(participant)
                .receiver(host)
                .notificationTitle(NotificationTitle.PARTICIPATION_TARDINESS)
                .content(PARTICIPATION_TARDINESS.formatted(participant.getNickname(), minute))
                .build();
    }
}
