package com.amorgakco.backend.notification.dto;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.NotificationType;
import com.amorgakco.backend.notification.domain.SendingType;
import lombok.Builder;

@Builder
public record NotificationRequest(
    String title, SendingType sendingType, String content, Member sender, Member receiver,
    Group group, NotificationType notificationType) {

}
