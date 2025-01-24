package com.amorgakco.backend.notification.infrastructure.publisher;


import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import com.amorgakco.backend.notification.dto.SmsMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsPublisher implements Publisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(final NotificationRequest notificationRequest) {
        final Member receiver = notificationRequest.receiver();
        if (receiver.isSmsNotificationActivated()) {
            final String phoneNumber = receiver.getPhoneNumber();
            convertAndSend(notificationRequest, phoneNumber);
        }
    }

    private void convertAndSend(final NotificationRequest request, final String phoneNumber) {
        rabbitTemplate.convertAndSend(
            ExchangeName.NOTIFICATION.getName(),
            RoutingKey.NOTIFICATION_SMS.getKey(),
            SmsMessageRequest.builder()
                .title(request.title())
                .content(request.content())
                .phoneNumber(phoneNumber)
                .build());
    }
}
