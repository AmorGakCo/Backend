package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository;
import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.FcmMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmPublisher implements Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void publish(final Notification notification) {
        final Member receiver = notification.getReceiver();
        fcmTokenRepository
            .findById(receiver.getId().toString())
            .ifPresent(
                token -> convertAndSend(notification, token.getToken()));
    }

    private void convertAndSend(final Notification notification, final String token) {
        rabbitTemplate.convertAndSend(
            ExchangeName.NOTIFICATION.getName(),
            RoutingKey.NOTIFICATION_FCM.getKey(),
            FcmMessageRequest.builder()
                .title(notification.getTitle())
                .notificationId(notification.getId())
                .content(notification.getContent())
                .token(token));
    }
}
