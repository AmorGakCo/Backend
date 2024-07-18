package com.amorgakco.backend.member.dto;

public record AdditionalInfoRequest(
        String githubUrl, String phoneNumber, String smsNotificationSetting) {}
