package com.amorgakco.backend.notification.domain;

import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationSubject notificationSubject;

    private String title;

    @OneToOne private Member receiver;

    private String content;
}
