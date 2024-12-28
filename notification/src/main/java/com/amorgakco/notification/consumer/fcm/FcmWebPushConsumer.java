package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.dto.FcmMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.rabbitmq.client.Channel;

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
    public void send(final org.springframework.amqp.core.Message rabbitMessage,
        final Channel channel) throws IOException{
        final FcmMessageRequest request = objectMapper.readValue(rabbitMessage.getBody(),
            FcmMessageRequest.class);
        long deliveryTag = rabbitMessage.getMessageProperties().getDeliveryTag();

        final Message message = createFcmMessage(request.token(), request.title(), request.content());
        try{
            FirebaseMessaging.getInstance().send(message);
            channel.basicAck(deliveryTag,false);
        }catch (FirebaseMessagingException e){
            log.info("RabbitMQ Nacked FCM Notification : {}",request);
            channel.basicNack(deliveryTag,false,false);
        }
    }

    public Message createFcmMessage(final String token, final String title, final String content) {
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
