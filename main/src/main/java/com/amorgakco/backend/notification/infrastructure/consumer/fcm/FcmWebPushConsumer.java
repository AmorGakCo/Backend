package com.amorgakco.backend.notification.infrastructure.consumer.fcm;

import com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository;
import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import com.amorgakco.backend.notification.infrastructure.consumer.slack.SlackSender;
import com.amorgakco.backend.notification.repository.NotificationRepository;
import com.amorgakco.backend.notification.service.mapper.NotificationMapper;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmWebPushConsumer {

    private final FcmTokenRepository fcmTokenRepository;
    private final SlackSender slackSender;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @RabbitListener(queues = "fcm")
    public void send(final NotificationRequest request) {
        final Notification notification = notificationMapper.toNotification(request);
        notificationRepository.save(notification);
        slackSender.sendNotification(request);
        //        fcmTokenRepository
        //                .findById(request.receiver().getId().toString())
        //                .ifPresent(
        //                        token ->
        //                                createMessage(
        //                                        token.getToken(),
        //                                        request.notificationContent().getTitle(),
        //                                        request.content()));
    }

    public Message createMessage(final String token, final String title, final String content) {
        return Message.builder()
            .setWebpushConfig(
                WebpushConfig.builder()
                    .setNotification(
                        WebpushNotification.builder()
                            .setTitle(title)
                            .setBody(content)
                            .build())
                    .build())
            .setToken(token)
            .build();
    }
}
