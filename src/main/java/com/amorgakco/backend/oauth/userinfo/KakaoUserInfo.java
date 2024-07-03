package com.amorgakco.backend.oauth.userinfo;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo {
    public KakaoUserInfo(final Map<String, Object> attributes) {
        final Map<String, Object> properties = (Map<String, Object>) attributes.get(PROPERTIES_KET);
        oauth2Id = attributes.get(USER_IDENTIFIER_KEY).toString();
        nickname = (String) properties.get(NICKNAME_KEY);
        imgUrl = (String) properties.get(IMG_URL_KEY);
    }

    private static final String PROPERTIES_KET = "properties";
    private static final String USER_IDENTIFIER_KEY = "id";
    private static final String NICKNAME_KEY = "nickname";
    private static final String IMG_URL_KEY = "profile_image";
    private final String oauth2Id;
    private final String nickname;
    private final String imgUrl;

    @Override
    public String getOauth2Id() {
        return oauth2Id;
    }

    @Override
    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
