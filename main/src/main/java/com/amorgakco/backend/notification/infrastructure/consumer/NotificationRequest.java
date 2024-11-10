package com.amorgakco.backend.notification.infrastructure.consumer;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.domain.SendingType;
import lombok.Builder;

@Builder
public record NotificationRequest(
    String title, SendingType sendingType, String content, Member receiver) {

}
