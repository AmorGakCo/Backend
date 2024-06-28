package com.amorgakco.backend.member.mapper;

import com.amorgakco.backend.member.domain.Account;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.oauth.userinfo.Oauth2UserInfo;

import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public Account toAccount(Member member, String provider, Oauth2UserInfo oauth2UserInfo) {
        return Account.builder()
                .member(member)
                .provider(provider)
                .identifier(oauth2UserInfo.getIdentifier())
                .imgUrl(oauth2UserInfo.getImgUrl())
                .nickname(oauth2UserInfo.getNickname())
                .build();
    }
}
