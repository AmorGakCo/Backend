package com.amorgakco.backend.notification.domain;

import lombok.RequiredArgsConstructor;

import static com.amorgakco.backend.notification.domain.NotificationType.*;

@RequiredArgsConstructor
public enum NotificationSubject {
    GROUP_JOINING_REQUEST("참여요청", FANOUT),
    GROUP_RECOMMEND("모각코 추천", FANOUT),
    MEMBER_TARDY("지각", FCM),
    GROUP_WITHDRAW("모각코 탈퇴", FCM);

    private final String subject;
    private final NotificationType notificationType;
}
