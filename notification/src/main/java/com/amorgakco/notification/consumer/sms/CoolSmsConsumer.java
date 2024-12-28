package com.amorgakco.notification.consumer.sms;

import com.amorgakco.notification.dto.SmsMessageRequest;
import com.amorgakco.notification.consumer.slack.SlackSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CoolSmsConsumer implements SmsConsumer {

    private final DefaultMessageService messageService;
    private final String serverPhoneNumber;
    private final SlackSender slackSender;
    private final ObjectMapper objectMapper;

    public CoolSmsConsumer(
            final DefaultMessageService messageService,
            @Value("${server-phone-number}") final String serverPhoneNumber,
            final SlackSender slackSender,
        final ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.serverPhoneNumber = serverPhoneNumber;
        this.slackSender = slackSender;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "sms")
    @Override
    public void consume(
            final String message)
            throws IOException {
        SmsMessageRequest request = objectMapper.readValue(message,
            SmsMessageRequest.class);
        slackSender.sendSmsMessage(request);
//        final long deliveryTag = envelope.getDeliveryTag();
//        try {
//            messageService.send(createFcmMessage(request));
//            channel.basicAck(deliveryTag, false);
//        } catch (NurigoEmptyResponseException
//                | NurigoMessageNotReceivedException
//                | NurigoUnknownException e) {
//            channel.basicNack(deliveryTag, false, false);
//        }
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
