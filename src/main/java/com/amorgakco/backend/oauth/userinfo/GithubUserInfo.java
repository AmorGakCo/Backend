package com.amorgakco.backend.oauth.userinfo;

import java.util.Map;

public class GithubUserInfo implements Oauth2UserInfo {
    public GithubUserInfo(Map<String, Object> attributes) {
        id = (String) attributes.get(USER_IDENTIFIER_KEY);
        nickname = (String) attributes.get(NICKNAME_KEY);
        imgUrl = (String) attributes.get(IMG_URL_KEY);
    }

    private static final String USER_IDENTIFIER_KEY = "id";
    private static final String NICKNAME_KEY = "login";
    private static final String IMG_URL_KEY = "avatar_url";
    private final String id;
    private final String nickname;
    private final String imgUrl;

    @Override
    public String getIdentifier() {
        return id;
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
