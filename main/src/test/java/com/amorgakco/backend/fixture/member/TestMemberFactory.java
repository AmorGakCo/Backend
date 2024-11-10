package com.amorgakco.backend.fixture.member;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.dto.PrivateMemberResponse;
import com.amorgakco.backend.oauth2.dto.Oauth2LoginResponse;
import com.amorgakco.backend.oauth2.dto.Oauth2MemberResponse;

public class TestMemberFactory {

    private static final Oauth2ProviderType OAUTH2_PROVIDER = Oauth2ProviderType.KAKAO;
    private static final String OAUTH2_ID = "2910293";
    private static final String IMG_URL = "https://fakeimg";
    private static final String NICKNAME = "아모르겠고";
    private static final String GITHUB_URL = "https://github/songhaechan";
    private static final String PHONE_NUMBER = "01012345678";
    private static final double LONGITUDE = 128.3245;
    private static final double LATITUDE = 37.3243;
    private static final Integer MOGAKCO_TEMPERATURE = 50;

    public static Member create(final Long id) {
        return new TestMember(id, OAUTH2_PROVIDER, OAUTH2_ID, IMG_URL, NICKNAME);
    }

    public static Member createEntity() {
        return Member.builder()
            .nickname(NICKNAME)
            .oauth2Id(OAUTH2_ID)
            .imgUrl(IMG_URL)
            .oauth2ProviderType(OAUTH2_PROVIDER)
            .build();
    }

    public static AdditionalInfoRequest createAdditionalInfoRequest(
        final boolean smsNotificationSetting) {
        return AdditionalInfoRequest.builder()
            .githubUrl(GITHUB_URL)
            .phoneNumber(PHONE_NUMBER)
            .smsNotificationSetting(smsNotificationSetting)
            .latitude(LATITUDE)
            .longitude(LONGITUDE)
            .build();
    }

    public static PrivateMemberResponse privateMemberResponse() {
        return PrivateMemberResponse.builder()
            .githubUrl(GITHUB_URL)
            .phoneNumber(PHONE_NUMBER)
            .moGakCoTemperature(MOGAKCO_TEMPERATURE)
            .smsNotificationSetting(true)
            .build();
    }

    public static Oauth2MemberResponse oauth2MemberResponse(final String memberId) {
        return Oauth2MemberResponse.builder()
            .imgUrl(IMG_URL)
            .memberId(memberId)
            .nickname(NICKNAME)
            .build();
    }

    public static Oauth2LoginResponse loginResponse(
        final String memberId, final Oauth2MemberResponse oauth2MemberResponse) {
        return Oauth2LoginResponse.builder()
            .oauth2MemberResponse(oauth2MemberResponse)
            .accessToken("access-token")
            .build();
    }
}
