package com.amorgakco.backend.fcm.service;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void send() {
        rabbitTemplate.convertAndSend();
    }
}
