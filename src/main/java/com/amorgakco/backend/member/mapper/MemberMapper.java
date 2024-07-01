package com.amorgakco.backend.member.mapper;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.oauth.userinfo.Oauth2UserInfo;

public class MemberMapper {
    public static Member mapFrom(final String provider, final Oauth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .identifier(oauth2UserInfo.getIdentifier())
                .imgUrl(oauth2UserInfo.getImgUrl())
                .nickname(oauth2UserInfo.getNickname())
                .provider(provider)
                .role(Role.ROLE_MEMBER)
                .build();
    }

    private MemberMapper() {}
}
