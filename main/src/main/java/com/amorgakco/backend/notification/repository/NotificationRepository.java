package com.amorgakco.backend.notification.repository;

import com.amorgakco.backend.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(
        "select n from Notification n where n.receiverId = :receiverId")
    Slice<Notification> findByReceiver(Long receiverId, Pageable pageable);
}
