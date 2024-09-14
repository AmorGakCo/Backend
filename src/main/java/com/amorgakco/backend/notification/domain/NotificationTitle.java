package com.amorgakco.backend.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationTitle {
    PARTICIPATION_REQUEST("참여 요청"),
    PARTICIPATION_APPROVED("참여 허가"),
    PARTICIPATION_REJECTED("참여 거절"),
    PARTICIPATION_WITHDRAW("모각코 탈퇴"),
    PARTICIPATION_TARDINESS("모각코 지각");

    private final String title;
}
