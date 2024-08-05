package com.amorgakco.backend.notification.service.consumer.sms;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import com.amorgakco.backend.notification.repository.NotificationRepository;
import com.amorgakco.backend.notification.service.mapper.NotificationMapper;

import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoolSmsConsumer implements SmsSender {

    private final DefaultMessageService messageService;
    private final String senderPhoneNumber;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public CoolSmsConsumer(
            final DefaultMessageService messageService,
            @Value("${server-phone-number}") final String senderPhoneNumber,
            final NotificationRepository notificationRepository,
            final NotificationMapper notificationMapper) {
        this.messageService = messageService;
        this.senderPhoneNumber = senderPhoneNumber;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @RabbitListener(queues = "notification.sms")
    public void send(final NotificationRequest request) {
        final Message message = createMessage(request);
        messageService.sendOne(new SingleMessageSendingRequest(message));
        notificationRepository.save(notificationMapper.toSmsNotification(request));
    }

    private Message createMessage(final NotificationRequest request) {
        final Message message = new Message();
        final Member receiver = request.receiver();
        message.setFrom(senderPhoneNumber);
        message.setTo(receiver.getPhoneNumber());
        message.setText(request.notificationTitle().getTitle() + "\n");
        message.setText(request.content());
        return message;
    }
}
