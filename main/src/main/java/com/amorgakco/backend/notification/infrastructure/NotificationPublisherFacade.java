package com.amorgakco.backend.notification.infrastructure;

import com.amorgakco.backend.notification.domain.SendingType;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import com.amorgakco.backend.notification.infrastructure.publisher.FcmPublisher;
import com.amorgakco.backend.notification.infrastructure.publisher.Publisher;
import com.amorgakco.backend.notification.infrastructure.publisher.SmsAndFcmPublisher;
import com.amorgakco.backend.notification.infrastructure.publisher.SmsPublisher;
import java.util.EnumMap;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisherFacade {

    private final EnumMap<SendingType, Publisher> publishers = new EnumMap<>(SendingType.class);

    public NotificationPublisherFacade(
        final SmsPublisher smsPublisher,
        final FcmPublisher fcmPublisher,
        final SmsAndFcmPublisher smsAndFcmPublisher
    ) {
        publishers.put(SendingType.SMS, smsPublisher);
        publishers.put(SendingType.WEB_PUSH, fcmPublisher);
        publishers.put(SendingType.SMS_AND_WEB_PUSH, smsAndFcmPublisher);
    }

    public void send(final NotificationRequest request) {
        final Publisher publisher = publishers.get(request.sendingType());
        publisher.publish(request);
    }
}
