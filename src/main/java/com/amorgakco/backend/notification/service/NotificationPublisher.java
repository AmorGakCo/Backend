package com.amorgakco.backend.notification.service;

import com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository;
import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.amorgakco.backend.notification.dto.NotificationRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final FcmTokenRepository fcmTokenRepository;

    public void sendSmsAndFcmWebPush(final NotificationRequest request) {
        sendSms(request);
        sendFcmWebPush(request);
    }

    public void sendSms(final NotificationRequest request) {
        if (request.receiver().isSmsNotificationActivated()) {
            rabbitTemplate.convertAndSend(
                    ExchangeName.NOTIFICATION.getName(),
                    RoutingKey.NOTIFICATION_SMS.getKey(),
                    request);
        }
    }

    public void sendFcmWebPush(final NotificationRequest request) {
        final Long receiverId = request.receiver().getId();
        fcmTokenRepository
                .findById(receiverId.toString())
                .ifPresent(
                        token ->
                                rabbitTemplate.convertAndSend(
                                        ExchangeName.NOTIFICATION.getName(),
                                        RoutingKey.NOTIFICATION_FCM.getKey(),
                                        request));
    }
}
