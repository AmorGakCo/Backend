package com.amorgakco.notification.dto;

public record SmsMessageRequest(
    Long notificationId,String title, String content, String phoneNumber
) {

}
