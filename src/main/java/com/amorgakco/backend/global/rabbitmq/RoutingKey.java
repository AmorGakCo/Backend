package com.amorgakco.backend.global.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoutingKey {
    NOTIFICATION_SMS("notification.sms"),
    NOTIFICATION_FCM("notification.fcm");

    private final String key;
}
