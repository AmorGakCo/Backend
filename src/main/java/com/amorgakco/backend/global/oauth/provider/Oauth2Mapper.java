package com.amorgakco.backend.global.oauth.provider;

import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;

import java.util.Map;

public interface Oauth2Mapper {
    Oauth2UserInfo toOauth2User(Map<String, Object> attributes);

    boolean isEqualsTo(String oauth2Provider);
}
