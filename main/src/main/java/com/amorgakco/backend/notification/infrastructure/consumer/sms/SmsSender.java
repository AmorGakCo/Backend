package com.amorgakco.backend.notification.infrastructure.consumer.sms;

import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;

public interface SmsSender {

    void send(final NotificationRequest notificationRequest);
}
