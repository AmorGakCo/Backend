package com.amorgakco.backend.member.mapper;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.global.oauth.userinfo.KakaoUserInfo;
import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.member.service.mapper.MemberMapper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class OauthUserInfoMapperTest {

    @Test
    void MapStruct를_사용해_객체를_매핑할수있다() {
        // given
        final String NICKNAME = "nickname";
        final String IMG = "img";
        final String ID = "1";
        final Map<String, Object> attributes = new HashMap<>();
        final Map<String, Object> properties = new HashMap<>();
        properties.put("nickname", NICKNAME);
        properties.put("profile_image", IMG);
        attributes.put("properties", properties);
        attributes.put("id", ID);
        final Oauth2UserInfo oauth2UserInfo = new KakaoUserInfo(attributes);
        // when
        final Member member =
                MemberMapper.INSTANCE.toMember(oauth2UserInfo, "kakao", Role.ROLE_MEMBER);
        // then
        assertThat(member.getOauth2Id()).isEqualTo(ID);
        assertThat(member.getImgUrl()).isEqualTo(IMG);
        assertThat(member.getNickname()).isEqualTo(NICKNAME);
        assertThat(member.getRoles()).isEqualTo(Role.ROLE_MEMBER);
    }
}
