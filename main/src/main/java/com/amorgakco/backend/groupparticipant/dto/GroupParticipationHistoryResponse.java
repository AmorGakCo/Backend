package com.amorgakco.backend.groupparticipant.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GroupParticipationHistoryResponse(
    int page,
    int elementSize,
    boolean hasNext,
    List<GroupParticipationHistory> histories
) {

}
