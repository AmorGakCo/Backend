package com.amorgakco.backend.oauth2.provider;

import com.amorgakco.backend.member.domain.Oauth2ProviderType;

import lombok.Builder;

@Builder
public record Oauth2Member(
        String oauth2Id, String imgUrl, String nickname, Oauth2ProviderType oauth2ProviderType) {}
