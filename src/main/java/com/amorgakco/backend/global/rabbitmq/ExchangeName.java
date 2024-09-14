package com.amorgakco.backend.global.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExchangeName {
    NOTIFICATION("notification"),
    NOTIFICATION_DEAD_LETTER("notification-dl");

    private final String name;
}
