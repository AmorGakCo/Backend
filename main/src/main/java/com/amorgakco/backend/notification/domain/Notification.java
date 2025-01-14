package com.amorgakco.backend.notification.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.group.domain.Group;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SendingType sendingType;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long senderMemberId;

    private Long receiverMemberId;

    @Builder
    public Notification(
        final String title,
        final String content,
        final SendingType sendingType, NotificationType notificationType,
        Long senderMemberId, Long receiverMemberId) {
        this.title = title;
        this.content = content;
        this.sendingType = sendingType;
        this.notificationType = notificationType;
        this.senderMemberId = senderMemberId;
        this.receiverMemberId = receiverMemberId;
    }
}
