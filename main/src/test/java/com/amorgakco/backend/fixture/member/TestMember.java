package com.amorgakco.backend.fixture.member;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import lombok.Getter;

@Getter
public class TestMember extends Member {

    private final Long id;

    public TestMember(
            final Long id,
            final Oauth2ProviderType oauth2ProviderType,
            final String oauth2Id,
            final String imgUrl,
            final String nickname) {
        super(oauth2ProviderType, oauth2Id, imgUrl, nickname);
        this.id = id;
    }

    @Override
    public boolean isEquals(final Long memberId) {
        return this.id.equals(memberId);
    }
}
