package com.amorgakco.backend.oauth.userinfo;

public interface Oauth2UserInfo {
    String getIdentifier();

    String getImgUrl();

    String getNickname();
}
