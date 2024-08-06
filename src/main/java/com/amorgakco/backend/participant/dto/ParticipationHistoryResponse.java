package com.amorgakco.backend.participant.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ParticipationHistoryResponse(
        int page,
        int elementSize,
        boolean hasNext,
        List<ParticipationHistory> activatedGroup,
        List<ParticipationHistory> InactivatedGroup) {}
