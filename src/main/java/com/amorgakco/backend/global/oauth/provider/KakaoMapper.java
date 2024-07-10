package com.amorgakco.backend.global.oauth.provider;

import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class KakaoMapper implements Oauth2Mapper {
    private static final String PROPERTIES_KET = "properties";
    private static final String USER_IDENTIFIER_KEY = "id";
    private static final String NICKNAME_KEY = "nickname";
    private static final String IMG_URL_KEY = "profile_image";

    @Override
    public Oauth2UserInfo toOauth2User(final Map<String, Object> attributes) {
        final Map<String, Object> properties = (Map<String, Object>) attributes.get(PROPERTIES_KET);
        final String oauth2Id = attributes.get(USER_IDENTIFIER_KEY).toString();
        final String nickname = (String) properties.get(NICKNAME_KEY);
        final String imgUrl = (String) properties.get(IMG_URL_KEY);
        return new Oauth2UserInfo(oauth2Id, nickname, imgUrl, Oauth2Provider.KAKAO);
    }

    @Override
    public boolean isEqualsTo(final String oauth2Provider) {
        return Objects.equals(
                Oauth2Provider.KAKAO.name().toLowerCase(), oauth2Provider.toLowerCase());
    }
}
