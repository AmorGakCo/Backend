package com.amorgakco.backend.notification.dto;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationTitle;

import lombok.Builder;

@Builder
public record NotificationRequest(
        NotificationTitle notificationTitle, Member sender, Member receiver, String content) {}
