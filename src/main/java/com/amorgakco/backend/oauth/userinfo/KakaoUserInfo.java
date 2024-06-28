package com.amorgakco.backend.oauth.userinfo;

import java.util.Map;

public class KakaoUserInfo implements Oauth2UserInfo {
    public KakaoUserInfo(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get(ACCOUNT_KEY);
        Map<String, Object> profile = (Map<String, Object>) account.get(PROFILE_KEY);
        id = (Long) attributes.get(USER_IDENTIFIER_KEY);
        nickname = (String) profile.get(NICKNAME_KEY);
        imgUrl = (String) profile.get(IMG_URL_KEY);
    }

    private static final String ACCOUNT_KEY = "kakao_acount";
    private static final String PROFILE_KEY = "profile";
    private static final String USER_IDENTIFIER_KEY = "id";
    private static final String NICKNAME_KEY = "nickname";
    private static final String IMG_URL_KEY = "profile_image_url";
    private final Long id;
    private final String nickname;
    private final String imgUrl;

    @Override
    public String getIdentifier() {
        return id.toString();
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
