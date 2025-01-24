package com.amorgakco.backend.notification.repository;

import com.amorgakco.backend.notification.domain.Notification;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMemoryRepository {

    private static final int BULK_INSERT_SIZE = 500;
    private final NotificationRepository notificationRepository;
    private final Lock lock = new ReentrantLock();
    private List<Notification> notificationQueue = new LinkedList<>();

    public void save(final Notification notification) {
        lock.lock();
        try {
            if (notificationQueue.size() >= BULK_INSERT_SIZE) {
                syncNotificationQueue();
            }
            notificationQueue.add(notification);
        } finally {
            lock.unlock();
        }
    }

    private void syncNotificationQueue() {
        notificationRepository.saveAll(notificationQueue);
        notificationQueue = new LinkedList<>();
    }

    public void flush() {
        syncNotificationQueue();
    }

}
