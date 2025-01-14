package com.amorgakco.backend.notification.repository;

import com.amorgakco.backend.notification.domain.Notification;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query(
        "select n from Notification n join fetch n.receiver join fetch n.sender join fetch n"
            + ".group where n.receiver.id = :receiverId")
    Slice<Notification> findByReceiver(Long receiverId, Pageable pageable);
}
