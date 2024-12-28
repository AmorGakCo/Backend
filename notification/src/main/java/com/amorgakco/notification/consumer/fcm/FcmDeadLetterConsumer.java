package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.consts.ExchangeName;
import com.amorgakco.notification.consts.RoutingKey;
import com.amorgakco.notification.consumer.slack.SlackSender;

import com.amorgakco.notification.dto.FcmMessageRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

@RequiredArgsConstructor
public class FcmDeadLetterConsumer {
    private static final Integer RETRY_THRESHOLD = 2;
    private final RabbitTemplate rabbitTemplate;
    private final SlackSender slackSender;

    @RabbitListener(queues = "fcm.deadletter")
    public void send(@Payload final FcmMessageRequest fcmMessageRequest,
        @Header("x-retries-count") Integer retryCount) throws IOException {
        if(retryCount>RETRY_THRESHOLD){
            sendSlack(fcmMessageRequest,retryCount);
        }
        rabbitTemplate.convertAndSend(ExchangeName.NOTIFICATION_DELAY.getName(),
            RoutingKey.NOTIFICATION_FCM_DELAY.getKey(),fcmMessageRequest);
    }

    public void sendSlack(final FcmMessageRequest failedMessage, final Integer retryCount){
        slackSender.sendFailedMessage(failedMessage.toString(),retryCount);
    }
}
