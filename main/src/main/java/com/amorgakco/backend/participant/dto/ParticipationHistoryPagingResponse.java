package com.amorgakco.backend.participant.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ParticipationHistoryPagingResponse(
        int page,
        int elementSize,
        boolean hasNext,
        List<ParticipationHistory> histories
) {
}
