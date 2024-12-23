package com.amorgakco.backend.oauth2.service;

import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.member.service.MemberService;
import com.amorgakco.backend.oauth2.dto.Oauth2MemberResponse;
import com.amorgakco.backend.oauth2.provider.Oauth2Member;
import com.amorgakco.backend.oauth2.provider.Oauth2ProviderSelector;
import com.amorgakco.backend.oauth2.provider.Oauth2ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Oauth2Service {

    private final Oauth2ProviderSelector oauth2ProviderSelector;
    private final MemberService memberService;

    public String getRedirectionLoginUrl(final Oauth2ProviderType oauth2ProviderType) {
        final Oauth2ProviderService providerService =
            oauth2ProviderSelector.getProvider(oauth2ProviderType);
        return providerService.getRedirectionLoginUrl();
    }

    public Oauth2MemberResponse login(
        final Oauth2ProviderType oauth2ProviderType, final String authCode,
        final String redirectUrl) {
        final Oauth2ProviderService providerService =
            oauth2ProviderSelector.getProvider(oauth2ProviderType);
        final Oauth2Member oauth2Member = providerService.getOauth2Member(authCode, redirectUrl);
        final Long memberId = memberService.updateOrSave(oauth2Member);
        return new Oauth2MemberResponse(
            memberId.toString(), oauth2Member.nickname(), oauth2Member.imgUrl());
    }
}
