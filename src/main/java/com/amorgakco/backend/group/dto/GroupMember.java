package com.amorgakco.backend.group.dto;

import lombok.Builder;

@Builder
public record GroupMember(
        Long memberId,
        String imgUrl,
        String nickname,
        Integer moGakCoTemperature,
        String githubUrl) {}
