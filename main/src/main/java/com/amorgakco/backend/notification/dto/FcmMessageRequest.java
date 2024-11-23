package com.amorgakco.backend.notification.dto;

public record FcmMessageRequest(
    Long notificationId, String title, String content, String token
) {

}
