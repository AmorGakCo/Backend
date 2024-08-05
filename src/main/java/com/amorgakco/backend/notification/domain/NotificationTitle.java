package com.amorgakco.backend.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationTitle {
    GROUP_JOINING_REQUEST("참여요청");

    private final String title;
}
