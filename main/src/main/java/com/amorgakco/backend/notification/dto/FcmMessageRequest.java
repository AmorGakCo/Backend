package com.amorgakco.backend.notification.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmMessageRequest {
    private Long notificationId;
    private String title;
    private String content;
    private String token;
}
