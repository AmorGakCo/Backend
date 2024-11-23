package com.amorgakco.notification.consts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QueueName {
    SMS("sms"),
    FCM("fcm");

    private final String name;
}
