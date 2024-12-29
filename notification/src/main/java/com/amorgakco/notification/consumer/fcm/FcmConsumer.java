package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.consumer.NotificationException;
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
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmConsumer {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "fcm")
    public void send(
            org.springframework.amqp.core.Message fcmMessage,
            final Channel channel)
            throws IOException, FirebaseMessagingException {
        FcmMessageRequest fcmMessageRequest =
                objectMapper.readValue(fcmMessage.getBody(), FcmMessageRequest.class);
        final Message message = createFcmMessage(fcmMessageRequest);
        if (fcmMessageRequest.getNotificationId() % 2 == 0) {
            try {
                throw new NotificationException();
            } catch (NotificationException e) {
                log.info("RabbitMQ Nacked FCM Notification : {}", fcmMessage);
                channel.basicNack(fcmMessage.getMessageProperties().getDeliveryTag(), false, false);
            }
        } else {
            log.info("ack {}", message);
            FirebaseMessaging.getInstance().send(message);
            channel.basicAck(fcmMessage.getMessageProperties().getDeliveryTag(), false);
        }
        //        try{
        //            FirebaseMessaging.getInstance().send(message);
        //            channel.basicAck(deliveryTag,false);
        //        }catch (FirebaseMessagingException e){
        //            log.info("RabbitMQ Nacked FCM Notification : {}",fcmMessageJson);
        //            channel.basicNack(deliveryTag,false,false);
        //        }
    }

    public Message createFcmMessage(final FcmMessageRequest request) {
        return Message.builder()
                .setWebpushConfig(
                        WebpushConfig.builder()
                                .setNotification(
                                        WebpushNotification.builder()
                                                .setTitle(request.getTitle())
                                                .setBody(request.getContent())
                                                .build())
                                .build())
                .setToken(request.getToken())
                .build();
    }
}
