package com.amorgakco.backend.notification.infrastructure.consumer;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationTitle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationCreator {

    public static NotificationRequest groupJoiningNotification(
            final Member sender, final Member receiver) {
        return NotificationRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .notificationTitle(NotificationTitle.GROUP_JOINING_REQUEST)
                .content(sender.getNickname() + "님 께서 모각코에 참여하길 원합니다.")
                .build();
    }
}
