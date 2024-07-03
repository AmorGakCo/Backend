package com.amorgakco.backend.oauth.service.mapper;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.oauth.userinfo.Oauth2UserInfo;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Oauth2Mapper {

    Oauth2Mapper INSTANCE = Mappers.getMapper(Oauth2Mapper.class);

    Member toMember(Oauth2UserInfo oauth2UserInfo, String provider, Role role);
}
