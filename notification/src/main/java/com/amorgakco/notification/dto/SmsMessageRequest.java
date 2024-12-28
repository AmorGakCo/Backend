package com.amorgakco.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessageRequest {
    private Long notificationId;
    private String title;
    private String content;
    private String phoneNumber;
}
