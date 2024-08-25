package com.amorgakco.backend.member.service.mapper;

import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.LoginResponse;
import com.amorgakco.backend.member.dto.PrivateMemberResponse;

import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public LoginResponse toLoginResponse(final Member member, final String accessToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .imgUrl(member.getImgUrl())
                .nickname(member.getNickname())
                .build();
    }

    public Member toMember(final Oauth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .oauth2Provider(oauth2UserInfo.oauth2Provider())
                .oauth2Id(oauth2UserInfo.oauth2Id())
                .nickname(oauth2UserInfo.nickname())
                .imgUrl(oauth2UserInfo.imgUrl())
                .build();
    }

    public PrivateMemberResponse toPrivateMemberResponse(final Member member) {
        return PrivateMemberResponse.builder()
                .moGakCoTemperature(member.getMoGakCoTemperature())
                .phoneNumber(member.getPhoneNumber())
                .githubUrl(member.getGithubUrl())
                .smsNotificationSetting(member.isSmsNotificationSetting())
                .build();
    }
}
