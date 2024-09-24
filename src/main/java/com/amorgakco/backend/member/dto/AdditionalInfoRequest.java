package com.amorgakco.backend.member.dto;

import lombok.Builder;

@Builder
public record AdditionalInfoRequest(
        String githubUrl,
        String phoneNumber,
        boolean smsNotificationSetting,
        double longitude,
        double latitude) {
}
