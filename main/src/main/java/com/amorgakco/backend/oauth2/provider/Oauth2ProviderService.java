package com.amorgakco.backend.oauth2.provider;

import com.amorgakco.backend.member.domain.Oauth2ProviderType;

public interface Oauth2ProviderService {

    Oauth2ProviderType getOauth2ProviderType();

    String getRedirectionLoginUrl();

    Oauth2Member getOauth2Member(String authCode, String redirectUrl);
}
