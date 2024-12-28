package com.amorgakco.notification.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExchangeName {
    NOTIFICATION("notification"),
    NOTIFICATION_DEAD_LETTER("notification.dlx"),
    NOTIFICATION_DELAY("notification.delay");

    private final String name;
}
