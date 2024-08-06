package com.amorgakco.backend.group.dto;

import lombok.Builder;

@Builder
public record GroupMember(
        String imgUrl, String nickname, Integer moGakCoTemperature, String githubUrl) {}
