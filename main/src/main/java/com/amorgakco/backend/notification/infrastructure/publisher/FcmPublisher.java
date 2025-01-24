package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository;
import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.FcmMessageRequest;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmPublisher implements Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void publish(final NotificationRequest notificationRequest) {
        fcmTokenRepository
            .findById(notificationRequest.receiver().getId().toString())
            .ifPresent(
                token -> convertAndSend(notificationRequest, token.getToken()));
    }

    private void convertAndSend(final NotificationRequest request, final String token) {
        rabbitTemplate.convertAndSend(
            ExchangeName.NOTIFICATION.getName(),
            RoutingKey.NOTIFICATION_FCM.getKey(),
            FcmMessageRequest.builder()
                .title(request.title())
                .content(request.content())
                .token(token)
                .build());
    }
}
