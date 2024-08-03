package com.amorgakco.backend.notification.receiver.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FcmWebPushReceiver {

    @RabbitListener(queues = "notification.fcm")
    public void send(final org.springframework.amqp.core.Message message) {
        final Message notification =
                Message.builder()
                        .setWebpushConfig(
                                WebpushConfig.builder()
                                        .setNotification(
                                                WebpushNotification.builder()
                                                        .setTitle("참여요청")
                                                        .setBody("aefef")
                                                        .build())
                                        .build())
                        .setToken(token)
                        .build();
        FirebaseMessaging.getInstance().send(notification);
    }
}
