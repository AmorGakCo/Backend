package com.amorgakco.backend.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class SmsMessageRequest{
    private Long notificationId;
    private String title;
    private String content;
    private String phoneNumber;
}
