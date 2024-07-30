package com.amorgakco.backend.fixture.member;

import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;

public class TestMemberFactory {

    private static final Oauth2Provider OAUTH2_PROVIDER = Oauth2Provider.KAKAO;
    private static final String OAUTH2_ID = "2910293";
    private static final String IMG_URL = "https://fakeimg";
    private static final String NICKNAME = "아모르겠고";
    private static final String GITHUB_URL = "https://github/songhaechan";
    private static final String PHONE_NUMBER = "01012345678";
    private static final double LONGITUDE = 128.3245;
    private static final double LATITUDE = 37.3243;

    public static Member create(final Long id) {
        return new TestMember(id, OAUTH2_PROVIDER, OAUTH2_ID, IMG_URL, NICKNAME);
    }

    public static AdditionalInfoRequest createAdditionalInfoRequest(
            final String smsNotificationSetting) {
        return AdditionalInfoRequest.builder()
                .githubUrl(GITHUB_URL)
                .phoneNumber(PHONE_NUMBER)
                .smsNotificationSetting(smsNotificationSetting)
                .latitude(LATITUDE)
                .longitude(LONGITUDE)
                .build();
    }
}
