package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.NotificationRequest;

public interface Publisher {

    void publish(NotificationRequest notificationRequest);
}
