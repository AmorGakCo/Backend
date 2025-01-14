package com.amorgakco.backend.notification.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
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

    private Member sender;

    private Member receiver;

    private Group group;

    @Builder
    public Notification(
        final String title, Member sender,
        final String content,
        final Member receiver,
        final SendingType sendingType, NotificationType notificationType, Group group) {
        this.title = title;
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        this.sendingType = sendingType;
        this.notificationType = notificationType;
        this.group = group;
    }
}
