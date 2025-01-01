package com.amorgakco.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FcmMessageRequest {
    private Long notificationId;
    private String title;
    private String content;
    private String token;
}
