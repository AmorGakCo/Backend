package com.amorgakco.backend.notification.domain;

import com.amorgakco.backend.global.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
public class Notification extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SendingType sendingType;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long senderId;

    private boolean isSmsNotificationActivated;

    private String phoneNumber;

    private Long receiverId;

    @Builder
    public Notification(
        final String title,
        final String content,
        final SendingType sendingType, NotificationType notificationType,
        Long senderId, boolean isSmsNotificationActivated, String phoneNumber, Long receiverId) {
        this.title = title;
        this.content = content;
        this.sendingType = sendingType;
        this.notificationType = notificationType;
        this.senderId = senderId;
        this.isSmsNotificationActivated = isSmsNotificationActivated;
        this.phoneNumber = phoneNumber;
        this.receiverId = receiverId;
    }
}
