package com.amorgakco.backend.global.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExchangeName {
    NOTIFICATION("notification");

    private final String name;
}
