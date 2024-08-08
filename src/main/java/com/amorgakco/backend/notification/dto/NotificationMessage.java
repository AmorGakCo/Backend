package com.amorgakco.backend.notification.dto;

import lombok.Builder;

@Builder
public record NotificationMessage(String title, String content) {}
