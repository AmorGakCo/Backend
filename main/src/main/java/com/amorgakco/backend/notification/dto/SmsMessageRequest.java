package com.amorgakco.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessageRequest {
    private String notificationId;
    private String title;
    private String content;
    private String phoneNumber;
}
