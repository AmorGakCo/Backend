package com.amorgakco.backend.groupparticipant.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupParticipationHistory(
        Long groupId, String name, String address, LocalDateTime beginAt, LocalDateTime endAt) {
}
