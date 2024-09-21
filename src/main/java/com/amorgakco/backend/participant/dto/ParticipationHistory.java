package com.amorgakco.backend.participant.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ParticipationHistory(
        Long groupId, String name, String address, LocalDateTime beginAt, LocalDateTime endAt) {
}
