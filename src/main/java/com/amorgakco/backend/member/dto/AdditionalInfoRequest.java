package com.amorgakco.backend.member.dto;

import lombok.Builder;

@Builder
public record AdditionalInfoRequest(
        String githubUrl,
        String phoneNumber,
        String smsNotificationSetting,
        double longitude,
        double latitude) {}
