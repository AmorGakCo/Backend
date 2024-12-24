package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.consumer.slack.SlackSender;
import com.amorgakco.notification.dto.FcmMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmWebPushConsumer {

    private final ObjectMapper objectMapper;


    @RabbitListener(queues = "fcm")
    public void send(final String notification) throws IOException, ExecutionException, InterruptedException {
        final FcmMessageRequest request = objectMapper.readValue(notification,
            FcmMessageRequest.class);
        final Message message = createMessage(request.token(), request.title(), request.content());
        final String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        log.info(response);
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
