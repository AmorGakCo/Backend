package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.consumer.slack.SlackSender;
import com.amorgakco.notification.dto.FcmMessageRequest;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmWebPushConsumer {

    private final SlackSender slackSender;

    @RabbitListener(queues = "fcm")
    public void send(final FcmMessageRequest request) {
        createMessage(request.token(),request.title(),request.content());
    }

    public Message createMessage(final String token, final String title, final String content) {
        return Message.builder()
                .setWebpushConfig(
                        WebpushConfig.builder()
                                .setNotification(
                                        WebpushNotification.builder()
                                                .setTitle(title)
                                                .setBody(content)
                                                .build())
                                .build())
                .setToken(token)
                .build();
    }
}
