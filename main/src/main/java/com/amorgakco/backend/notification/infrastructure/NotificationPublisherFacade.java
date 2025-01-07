package com.amorgakco.backend.notification.infrastructure;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.domain.SendingType;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import com.amorgakco.backend.notification.infrastructure.publisher.FcmPublisher;
import com.amorgakco.backend.notification.infrastructure.publisher.Publisher;
import com.amorgakco.backend.notification.infrastructure.publisher.SmsAndFcmPublisher;
import com.amorgakco.backend.notification.infrastructure.publisher.SmsPublisher;
import com.amorgakco.backend.notification.repository.NotificationRepository;
import com.amorgakco.backend.notification.service.mapper.NotificationMapper;
import java.util.EnumMap;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisherFacade {

    private final EnumMap<SendingType, Publisher> publishers = new EnumMap<>(SendingType.class);
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationPublisherFacade(
        final SmsPublisher smsPublisher,
        final FcmPublisher fcmPublisher,
        final SmsAndFcmPublisher smsAndFcmPublisher,
        final NotificationRepository notificationRepository,
        final NotificationMapper notificationMapper
    ) {
        publishers.put(SendingType.SMS, smsPublisher);
        publishers.put(SendingType.WEB_PUSH, fcmPublisher);
        publishers.put(SendingType.SMS_AND_WEB_PUSH, smsAndFcmPublisher);
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public void send(final Notification notification) {
        final Publisher publisher = publishers.get(notification.getSendingType());
//        final Notification notification = notificationMapper.toNotification(request);
//        notificationRepository.save(notification);
        publisher.publish(notification);
    }

    public void send(final NotificationRequest notificationRequest) {
        final Publisher publisher = publishers.get(notificationRequest.sendingType());
        final Notification notification = notificationMapper.toNotification(notificationRequest);
        notificationRepository.save(notification);
        publisher.publish(notification);
    }
}
