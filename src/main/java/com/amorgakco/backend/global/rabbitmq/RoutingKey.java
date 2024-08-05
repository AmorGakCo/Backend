package com.amorgakco.backend.global.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoutingKey {
    DELIMITER("."),
    NOTIFICATION_SMS(ExchangeName.NOTIFICATION.getName() + DELIMITER.key + QueueName.SMS.getName()),
    NOTIFICATION_FCM(ExchangeName.NOTIFICATION.getName() + DELIMITER.key + QueueName.FCM.getName());

    private final String key;
}
