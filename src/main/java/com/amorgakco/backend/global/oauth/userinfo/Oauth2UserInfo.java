package com.amorgakco.backend.global.oauth.userinfo;

import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;

public record Oauth2UserInfo(
        String oauth2Id, String nickname, String imgUrl, Oauth2Provider oauth2Provider) {}
