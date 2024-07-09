package com.amorgakco.backend.member.service.mapper;

import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;

import org.mapstruct.Mapper;

@Mapper
public interface MemberMapper {

    Member toMember(Oauth2UserInfo oauth2UserInfo);
}
