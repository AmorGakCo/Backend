package com.amorgakco.backend.oauth2.provider.kakao;

import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.oauth2.provider.Oauth2Member;
import com.amorgakco.backend.oauth2.provider.Oauth2ProviderService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2ProviderService implements Oauth2ProviderService {

    private static final String GRANT_TYPE = "grant_type";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String RESPONSE_TYPE = "response_type";
    private static final String CODE = "code";
    private static final String CLIENT_ID = "client_id";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String SCOPE = "scope";
    private static final String DELIMITER = ",";
    private static final String BEARER = "Bearer ";
    private final KakaoRestClient kakaoRestClient;
    private final KakaoOauth2Properties kakaoOauth2Properties;

    @Override
    public Oauth2ProviderType getOauth2ProviderType() {
        return Oauth2ProviderType.KAKAO;
    }

    @Override
    public String getRedirectionLoginUrl() {
        return UriComponentsBuilder.fromUriString(kakaoOauth2Properties.loginUri())
                .queryParam(RESPONSE_TYPE, CODE)
                .queryParam(CLIENT_ID, kakaoOauth2Properties.clientId())
                .queryParam(REDIRECT_URI, kakaoOauth2Properties.redirectUri())
                .queryParam(SCOPE, String.join(DELIMITER, kakaoOauth2Properties.scope()))
                .toUriString();
    }

    @Override
    public Oauth2Member getOauth2Member(final String authCode) {
        final KakaoAuthorization kakaoAuthorization =
                kakaoRestClient.getKakaoAccessToken(requestParams(authCode));
        final String accessToken = kakaoAuthorization.accessToken();
        final KakaoMemberResponse kakaoMember =
                kakaoRestClient.getKakaoMember(BEARER + accessToken);
        return kakaoMember.toOauth2Member();
    }

    private MultiValueMap<String, String> requestParams(final String authCode) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(GRANT_TYPE, AUTHORIZATION_CODE);
        params.add(CLIENT_ID, kakaoOauth2Properties.clientId());
        params.add(REDIRECT_URI, kakaoOauth2Properties.redirectUri());
        params.add(CODE, authCode);
        params.add(CLIENT_ID, kakaoOauth2Properties.clientSecret());
        return params;
    }
}
