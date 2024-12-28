package com.amorgakco.notification.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoutingKey {
    DELIMITER("."),
    NOTIFICATION_SMS(
            ExchangeName.NOTIFICATION.getName() + DELIMITER.key + QueueName.SMS_QUEUE.getName()),
    NOTIFICATION_FCM(
            ExchangeName.NOTIFICATION.getName() + DELIMITER.key + QueueName.FCM_QUEUE.getName()),
    NOTIFICATION_SMS_DEAD_LETTER(
            ExchangeName.NOTIFICATION_DEAD_LETTER.getName()
                    + DELIMITER.key
                    + QueueName.SMS_QUEUE.getName()),
    NOTIFICATION_FCM_DEAD_LETTER(
            ExchangeName.NOTIFICATION_DEAD_LETTER.getName()
                    + DELIMITER.key
                    + QueueName.FCM_QUEUE.getName()),
    NOTIFICATION_SMS_DELAY(
            ExchangeName.NOTIFICATION_DELAY.getName()
                    + DELIMITER.key
                    + QueueName.SMS_QUEUE.getName()),
    NOTIFICATION_FCM_DELAY(
            ExchangeName.NOTIFICATION_DELAY.getName()
                    + DELIMITER.key
                    + QueueName.FCM_QUEUE.getName());

    private final String key;
}
