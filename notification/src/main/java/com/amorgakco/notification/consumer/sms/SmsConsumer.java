package com.amorgakco.notification.consumer.sms;

import java.io.IOException;

public interface SmsConsumer {

    void consume(final String message)
            throws IOException;
}
