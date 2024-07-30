package com.amorgakco.backend.fixture.member;

import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;
import com.amorgakco.backend.member.domain.Member;

import lombok.Getter;

@Getter
public class TestMember extends Member {

    private final Long id;

    public TestMember(
            final Long id,
            final Oauth2Provider oauth2Provider,
            final String oauth2Id,
            final String imgUrl,
            final String nickname) {
        super(oauth2Provider, oauth2Id, imgUrl, nickname);
        this.id = id;
    }

    @Override
    public boolean isEquals(final Long memberId) {
        return this.id.equals(memberId);
    }
}
