package com.amorgakco.backend.notification.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTime {

    @Id @GeneratedValue private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationTitle notificationTitle;

    @OneToOne(fetch = FetchType.LAZY)
    private Member sender;

    @OneToOne(fetch = FetchType.LAZY)
    private Member receiver;

    private String content;

    @Builder
    public Notification(
            final NotificationTitle notificationTitle,
            final Member sender,
            final Member receiver,
            final String content) {
        this.notificationTitle = notificationTitle;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
