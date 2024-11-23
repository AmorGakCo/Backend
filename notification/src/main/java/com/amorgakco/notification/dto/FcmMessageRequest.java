package com.amorgakco.notification.dto;


public record FcmMessageRequest(
    Long notificationId,String title, String content, String token
) {

}
