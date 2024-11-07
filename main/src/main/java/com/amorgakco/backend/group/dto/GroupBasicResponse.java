package com.amorgakco.backend.group.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GroupBasicResponse(
    String hostNickname,
    String hostImgUrl,
    LocalDateTime beginAt,
    LocalDateTime endAt,
    int groupCapacity,
    int currentParticipants,
    String address,
    boolean isParticipated,
    boolean isParticipationRequested) {

}
