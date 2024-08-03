package com.amorgakco.backend.fcm.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SmsSender {

    @RabbitListener(queues = "notification.sms")
    public void sendSms() {}
}
