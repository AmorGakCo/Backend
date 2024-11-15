package com.amorgakco.notification.consumer.sms;

import com.amorgakco.notification.dto.SmsMessageRequest;

public interface SmsSender {

    void send(final SmsMessageRequest smsMessageRequest);
}
