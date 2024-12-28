package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.consts.ExchangeName;
import com.amorgakco.notification.consts.RoutingKey;
import com.amorgakco.notification.consumer.slack.SlackSender;
import com.amorgakco.notification.dto.FcmMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FcmDeadLetterConsumer {
    private static final Integer RETRY_THRESHOLD = 2;
    private final RabbitTemplate rabbitTemplate;
    private final SlackSender slackSender;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "fcm.deadletter")
    public void send(Message fcmMessage) throws IOException {
        FcmMessageRequest fcmMessageRequest =
                objectMapper.readValue(fcmMessage.getBody(), FcmMessageRequest.class);
        Integer retryCount =
                Optional.ofNullable(
                                (Integer)
                                        fcmMessage
                                                .getMessageProperties()
                                                .getHeaders()
                                                .get("x-retries-count"))
                        .orElse(1);
        if (retryCount > RETRY_THRESHOLD) {
            sendSlack(fcmMessageRequest, retryCount);
        } else {
            fcmMessage.getMessageProperties().getHeaders().put("x-retries-count", ++retryCount);
            rabbitTemplate.convertAndSend(
                    ExchangeName.NOTIFICATION_DELAY.getName(),
                    RoutingKey.NOTIFICATION_FCM_DELAY.getKey(),
                    fcmMessage);
        }
    }

    public void sendSlack(final FcmMessageRequest failedMessage, final Integer retryCount) {
        slackSender.sendFailedMessage(failedMessage.toString(), retryCount);
    }
}
