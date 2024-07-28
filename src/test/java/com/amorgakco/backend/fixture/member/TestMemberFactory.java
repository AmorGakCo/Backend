package com.amorgakco.backend.fixture.member;

import com.amorgakco.backend.fixture.group.TestMember;
import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;
import com.amorgakco.backend.member.domain.Member;

public class TestMemberFactory {

    public static Member create(final Long id) {
        return new TestMember(id, Oauth2Provider.KAKAO, "2910293", "https://fakeimg", "AGC1");
    }
}
