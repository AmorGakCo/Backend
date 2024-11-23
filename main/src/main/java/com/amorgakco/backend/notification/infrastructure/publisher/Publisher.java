package com.amorgakco.backend.notification.infrastructure.publisher;

import com.amorgakco.backend.notification.domain.Notification;

public interface Publisher {

    void publish(Notification notification);
}
