package com.amorgakco.backend.member.service.mapper;

import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;

import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toMember(final Oauth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .oauth2Provider(oauth2UserInfo.oauth2Provider())
                .oauth2Id(oauth2UserInfo.oauth2Id())
                .nickname(oauth2UserInfo.nickname())
                .imgUrl(oauth2UserInfo.imgUrl())
                .build();
    }
}
