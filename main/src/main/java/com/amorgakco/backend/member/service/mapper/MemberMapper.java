package com.amorgakco.backend.member.service.mapper;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.PrivateMemberResponse;
import com.amorgakco.backend.oauth2.provider.Oauth2Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toMember(final Oauth2Member oauth2Member) {
        return Member.builder()
                .oauth2ProviderType(oauth2Member.oauth2ProviderType())
                .oauth2Id(oauth2Member.oauth2Id())
                .nickname(oauth2Member.nickname())
                .imgUrl(oauth2Member.imgUrl())
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
