package com.amorgakco.notification.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QueueName {
    SMS_QUEUE("sms"),
    FCM_QUEUE("fcm"),
    SMS_DELAY_QUEUE("sms.delay"),
    FCM_DELAY_QUEUE("fcm.delay"),
    SMS_DEAD_LETTER_QUEUE("sms.deadletter"),
    FCM_DEAD_LETTER_QUEUE("fcm.deadletter");

    private final String name;
}
