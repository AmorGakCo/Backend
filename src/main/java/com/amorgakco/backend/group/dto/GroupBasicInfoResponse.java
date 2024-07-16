package com.amorgakco.backend.group.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupBasicInfoResponse(
        String hostNickname,
        String hostImgUrl,
        int hostPoint,
        String hostGitHubUrl,
        LocalDateTime beginTime,
        LocalDateTime endTime,
        int groupCapacity,
        int currentParticipants,
        String address) {}
