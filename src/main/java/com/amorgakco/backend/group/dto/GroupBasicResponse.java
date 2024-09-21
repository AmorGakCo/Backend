package com.amorgakco.backend.group.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupBasicResponse(
        String hostNickname,
        String hostImgUrl,
        LocalDateTime beginAt,
        LocalDateTime endAt,
        int groupCapacity,
        int currentParticipants,
        String address) {
}
