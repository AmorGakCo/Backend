package com.amorgakco.notification.consumer.sms;

import com.amorgakco.notification.dto.SmsMessageRequest;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import java.io.IOException;

public interface SmsConsumer {

    void consume(final SmsMessageRequest request, final Channel channel, final Envelope envelope)
            throws IOException;
}
