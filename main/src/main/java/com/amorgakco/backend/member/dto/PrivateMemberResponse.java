package com.amorgakco.backend.member.dto;

import lombok.Builder;

@Builder
public record PrivateMemberResponse(
        boolean smsNotificationSetting,
        String githubUrl,
        String phoneNumber,
        Integer moGakCoTemperature) {
}
