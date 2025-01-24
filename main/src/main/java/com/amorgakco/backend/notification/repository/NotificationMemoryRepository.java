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

    private final NotificationRepository notificationRepository;
    private List<Notification> notificationQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private static final int BULK_INSERT_SIZE = 500;

    public void save(final Notification notification){
        if(notificationQueue.size() >= BULK_INSERT_SIZE){
            syncNotificationQueue();
        }
        notificationQueue.add(notification);
    }

    private void syncNotificationQueue() {
        lock.lock();
        try{
            notificationRepository.saveAll(notificationQueue);
            notificationQueue = new LinkedList<>();
        }finally {
            lock.unlock();
        }
    }

    public void flush(){
        syncNotificationQueue();
    }

}
