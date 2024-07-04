package com.amorgakco.backend.global.oauth.service.mapper;

import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import org.mapstruct.Mapper;

@Mapper
public interface Oauth2Mapper {

    Member toMember(Oauth2UserInfo oauth2UserInfo, String provider, Role role);
}
