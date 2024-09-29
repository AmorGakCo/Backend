package com.amorgakco.backend.global.rabbitmq;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoutingKey {
    DELIMITER("."),
    NOTIFICATION_SMS(ExchangeName.NOTIFICATION.getName() + DELIMITER.key + QueueName.SMS.getName()),
    NOTIFICATION_FCM(ExchangeName.NOTIFICATION.getName() + DELIMITER.key + QueueName.FCM.getName()),
    NOTIFICATION_DEAD_LETTER(ExchangeName.NOTIFICATION_DEAD_LETTER.getName());

    private final String key;
}
