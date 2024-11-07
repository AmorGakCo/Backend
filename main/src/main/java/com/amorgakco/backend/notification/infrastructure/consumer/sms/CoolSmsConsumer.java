package com.amorgakco.backend.notification.infrastructure.consumer.sms;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import com.amorgakco.backend.notification.infrastructure.consumer.slack.SlackSender;
import com.amorgakco.backend.notification.repository.NotificationRepository;
import com.amorgakco.backend.notification.service.mapper.NotificationMapper;
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
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public CoolSmsConsumer(
        final DefaultMessageService messageService,
        @Value("${server-phone-number}") final String serverPhoneNumber,
        final SlackSender slackSender,
        final NotificationMapper notificationMapper,
        final NotificationRepository notificationRepository) {
        this.messageService = messageService;
        this.serverPhoneNumber = serverPhoneNumber;
        this.slackSender = slackSender;
        this.notificationMapper = notificationMapper;
        this.notificationRepository = notificationRepository;
    }

    @Override
    @RabbitListener(queues = "sms")
    public void send(final NotificationRequest request) {
        final Notification notification = notificationMapper.toNotification(request);
        notificationRepository.save(notification);
        slackSender.sendNotification(request);
        //        final Message message = createMessage(request);
        //        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private Message createMessage(final NotificationRequest request) {
        final Message message = new Message();
        final Member receiver = request.receiver();
        message.setFrom(serverPhoneNumber);
        message.setTo(receiver.getPhoneNumber());
        message.setText(request.title() + "\n");
        message.setText(request.content());
        return message;
    }
}
