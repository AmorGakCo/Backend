package com.amorgakco.backend.notification.receiver.sms;

import lombok.RequiredArgsConstructor;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoolSmsSender implements SmsSender {

    private final DefaultMessageService messageService;

    @Override
    @RabbitListener(queues = "notification.sms")
    public void send() {
        final Message message = new Message();
        message.setFrom("01087796871");
        message.setTo("01087715730");
        message.setText("ㅎㅇ승진 잘 가길 바라...");
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
