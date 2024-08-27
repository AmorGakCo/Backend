package com.amorgakco.backend.oauth2.provider.kakao;

import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.oauth2.provider.Oauth2Member;

public record KakaoMemberResponse(Long id, KakaoAccount kakaoAccount) {
    public Oauth2Member toOauth2Member() {
        return Oauth2Member.builder()
                .oauth2Id(this.id().toString())
                .nickname(this.kakaoAccount().profile().nickname())
                .imgUrl(this.kakaoAccount().profile().profileImageUrl())
                .oauth2ProviderType(Oauth2ProviderType.KAKAO)
                .build();
    }

    public record KakaoAccount(Profile profile) {}

    public record Profile(String nickname, String profileImageUrl) {}
}
