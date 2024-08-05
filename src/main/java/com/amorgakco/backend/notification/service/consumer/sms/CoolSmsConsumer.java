package com.amorgakco.backend.notification.service.consumer.sms;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.dto.NotificationRequest;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoolSmsConsumer implements SmsSender {

    private final DefaultMessageService messageService;
    private final String serverPhoneNumber;

    public CoolSmsConsumer(
            final DefaultMessageService messageService,
            @Value("${server-phone-number}") final String serverPhoneNumber) {
        this.messageService = messageService;
        this.serverPhoneNumber = serverPhoneNumber;
    }

    @Override
    @RabbitListener(queues = "notification.sms")
    public void send(final NotificationRequest request) {
        final Message message = createMessage(request);
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private Message createMessage(final NotificationRequest request) {
        final Message message = new Message();
        final Member receiver = request.receiver();
        message.setFrom(serverPhoneNumber);
        message.setTo(receiver.getPhoneNumber());
        message.setText(request.notificationTitle().getTitle() + "\n");
        message.setText(request.content());
        return message;
    }
}
