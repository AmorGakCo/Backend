package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository;
import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmPublisher implements Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void publish(final NotificationRequest request) {
        rabbitTemplate.convertAndSend(
            ExchangeName.NOTIFICATION.getName(), RoutingKey.NOTIFICATION_FCM.getKey(), request);
        //        final Long receiverId = request.receiver().getId();
        //        fcmTokenRepository
        //                .findById(receiverId.toString())
        //                .ifPresent(
        //                        token ->
        //                                rabbitTemplate.convertAndSend(
        //                                        ExchangeName.NOTIFICATION.getName(),
        //                                        RoutingKey.NOTIFICATION_FCM.getKey(),
        //                                        request));
    }
}
