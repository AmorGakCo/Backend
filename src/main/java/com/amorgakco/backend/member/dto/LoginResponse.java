package com.amorgakco.backend.member.dto;

import lombok.Builder;

@Builder
public record LoginResponse(String accessToken, String nickname, String imgUrl) {}
