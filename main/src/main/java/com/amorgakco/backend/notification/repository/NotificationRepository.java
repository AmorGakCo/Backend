package com.amorgakco.backend.notification.repository;

import com.amorgakco.backend.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

//    @Query(
//        "select n from Notification n join fetch n.receiver join fetch n.sender join fetch n"
//            + ".group where n.receiver.id = :receiverId")
//    Slice<Notification> findByReceiver(Long receiverId, Pageable pageable);
}
