package com.amorgakco.backend.notification.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record NotificationMessageResponse(
    int page,
    int elementSize,
    boolean hasNext,
    List<NotificationMessage> notificationMessages) {

}
