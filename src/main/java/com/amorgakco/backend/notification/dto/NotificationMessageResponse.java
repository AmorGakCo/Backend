package com.amorgakco.backend.notification.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationMessageResponse(
        int page,
        int elementSize,
        boolean hasNext,
        List<NotificationMessage> notificationMessages) {}
