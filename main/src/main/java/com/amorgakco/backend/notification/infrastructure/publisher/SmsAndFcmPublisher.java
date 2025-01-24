package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsAndFcmPublisher implements Publisher {

    private final SmsPublisher smsPublisher;
    private final FcmPublisher fcmPublisher;

    @Override
    public void publish(final NotificationRequest notificationRequest) {
        smsPublisher.publish(notificationRequest);
        fcmPublisher.publish(notificationRequest);
    }
}
