package com.amorgakco.notification.consumer.sms;

import com.amorgakco.notification.consumer.slack.SlackSender;
import com.amorgakco.notification.dto.SmsMessageRequest;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoolSmsConsumer implements SmsSender {

    private final DefaultMessageService messageService;
    private final String serverPhoneNumber;
    private final SlackSender slackSender;

    public CoolSmsConsumer(
            final DefaultMessageService messageService,
            @Value("${server-phone-number}") final String serverPhoneNumber,
            final SlackSender slackSender) {
        this.messageService = messageService;
        this.serverPhoneNumber = serverPhoneNumber;
        this.slackSender = slackSender;
    }

    @Override
    @RabbitListener(queues = "sms")
    public void send(final SmsMessageRequest request) {
        slackSender.sendSmsMessage(request);
        //        final Message message = createMessage(request);
        //        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private Message createMessage(final SmsMessageRequest request) {
        final Message message = new Message();
        message.setFrom(serverPhoneNumber);
        message.setTo(request.phoneNumber());
        message.setText(request.title() + "\n");
        message.setText(request.content());
        return message;
    }
}
