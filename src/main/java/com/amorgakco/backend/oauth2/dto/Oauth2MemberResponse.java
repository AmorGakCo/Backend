package com.amorgakco.backend.oauth2.dto;

import lombok.Builder;

@Builder
public record Oauth2MemberResponse(String memberId, String nickname, String imgUrl) {}
