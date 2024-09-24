package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;

public interface Publisher {
    void publish(NotificationRequest request);
}
