package com.amorgakco.backend.groupparticipant.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GroupParticipationHistory(
    Long groupId, String name, String address, LocalDateTime beginAt, LocalDateTime endAt) {

}
