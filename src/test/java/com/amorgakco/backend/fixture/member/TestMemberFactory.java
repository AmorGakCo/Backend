package com.amorgakco.backend.fixture.member;

import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;

public class TestMemberFactory {

    private static final Oauth2Provider oauth2Provider = Oauth2Provider.KAKAO;
    private static final String oauth2Id = "2910293";
    private static final String imgUrl = "https://fakeimg";
    private static final String nickName = "아모르겠고";
    private static final String githubUrl = "https://github/songhaechan";
    private static final String phoneNumber = "01012345678";
    private static final double longitude = 128.3245;
    private static final double latitude = 37.3243;

    public static Member create(final Long id) {
        return new TestMember(id, oauth2Provider, oauth2Id, imgUrl, nickName);
    }

    public static AdditionalInfoRequest createAdditionalInfoRequest(
            final String smsNotificationSetting) {
        return AdditionalInfoRequest.builder()
                .githubUrl(githubUrl)
                .phoneNumber(phoneNumber)
                .smsNotificationSetting(smsNotificationSetting)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
