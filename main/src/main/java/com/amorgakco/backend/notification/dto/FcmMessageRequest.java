package com.amorgakco.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmMessageRequest {
    private String notificationId;
    private String title;
    private String content;
    private String token;
}
