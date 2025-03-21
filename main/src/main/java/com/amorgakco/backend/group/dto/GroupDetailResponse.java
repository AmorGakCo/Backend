package com.amorgakco.backend.group.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GroupDetailResponse(
    GroupMember host,
    String name,
    String description,
    String address,
    double longitude,
    double latitude,
    LocalDateTime beginAt,
    LocalDateTime endAt,
    List<GroupMember> groupMembers,
    boolean isGroupHost) {

}
