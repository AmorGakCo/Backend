package com.amorgakco.backend.notification.infrastructure.publisher;


import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsPublisher implements Publisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(final NotificationRequest request) {
        if (request.receiver().isSmsNotificationActivated()) {
            rabbitTemplate.convertAndSend(
                ExchangeName.NOTIFICATION.getName(),
                RoutingKey.NOTIFICATION_SMS.getKey(),
                request);
        }
    }
}
