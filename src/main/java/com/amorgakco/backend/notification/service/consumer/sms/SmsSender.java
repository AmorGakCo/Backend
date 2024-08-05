package com.amorgakco.backend.notification.service.consumer.sms;

import com.amorgakco.backend.notification.dto.NotificationRequest;

public interface SmsSender {

    void send(final NotificationRequest notificationRequest);
}
