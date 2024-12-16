package com.amorgakco.backend.notification.dto;

public record SmsMessageRequest(
    Long notificationId, String title, String content, String phoneNumber
) {

}
