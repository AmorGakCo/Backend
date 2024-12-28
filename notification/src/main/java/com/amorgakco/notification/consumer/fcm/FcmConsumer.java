package com.amorgakco.notification.consumer.fcm;

import com.amorgakco.notification.consumer.NotificationException;
import com.amorgakco.notification.dto.FcmMessageRequest;
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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmConsumer {

    @RabbitListener(queues = "fcm")
    public void send(
        @Payload FcmMessageRequest fcmMessageRequest,
        @Header(AmqpHeaders.DELIVERY_TAG) final long deliveryTag,
        final Channel channel) throws IOException, FirebaseMessagingException {
        final Message message = createFcmMessage(fcmMessageRequest);

        if(fcmMessageRequest.notificationId() % 2 ==0){
            try{
                throw new NotificationException();
            }catch (NotificationException e){
                log.info("RabbitMQ Nacked FCM Notification : {}",fcmMessageRequest);
                channel.basicNack(deliveryTag,false,false);
            }
        }else {
            FirebaseMessaging.getInstance().send(message);
            channel.basicAck(deliveryTag,false);
        }

//        try{
//            FirebaseMessaging.getInstance().send(message);
//            channel.basicAck(deliveryTag,false);
//        }catch (FirebaseMessagingException e){
//            log.info("RabbitMQ Nacked FCM Notification : {}",fcmMessageRequest);
//            channel.basicNack(deliveryTag,false,false);
//        }
    }

    public Message createFcmMessage(final FcmMessageRequest request) {
        return Message.builder()
                .setWebpushConfig(
                        WebpushConfig.builder()
                                .setNotification(
                                        WebpushNotification.builder()
                                                .setTitle(request.title())
                                                .setBody(request.content())
                                                .build())
                                .build())
                .setToken(request.token())
                .build();
    }
}
