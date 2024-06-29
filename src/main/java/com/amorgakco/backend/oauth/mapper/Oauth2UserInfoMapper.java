package com.amorgakco.backend.oauth.mapper;

import com.amorgakco.backend.oauth.exception.UnsupportedOauth2ClientException;
import com.amorgakco.backend.oauth.userinfo.GithubUserInfo;
import com.amorgakco.backend.oauth.userinfo.KakaoUserInfo;
import com.amorgakco.backend.oauth.userinfo.Oauth2UserInfo;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Oauth2UserInfoMapper {
    private static final String KAKAO = "kakao";
    private static final String GITHUB = "github";

    public Oauth2UserInfo toDetailOauth2UserInfo(
            String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case KAKAO -> new KakaoUserInfo(attributes);
            case GITHUB -> new GithubUserInfo(attributes);
            default -> throw new UnsupportedOauth2ClientException();
        };
    }
}
