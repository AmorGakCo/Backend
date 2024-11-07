package com.amorgakco.backend.oauth2.dto;

import lombok.Builder;

@Builder
public record Oauth2LoginResponse(Oauth2MemberResponse oauth2MemberResponse, String accessToken) {

}
