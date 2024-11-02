package com.amorgakco.backend.groupparticipant.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupParticipationHistoryResponse(
        int page,
        int elementSize,
        boolean hasNext,
        List<GroupParticipationHistory> histories
) {
}
