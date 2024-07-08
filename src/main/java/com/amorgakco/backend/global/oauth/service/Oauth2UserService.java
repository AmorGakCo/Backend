package com.amorgakco.backend.global.oauth.service;

import com.amorgakco.backend.global.oauth.MemberPrincipal;
import com.amorgakco.backend.global.oauth.provider.Oauth2Selector;
import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.member.domain.RoleEntity;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.member.service.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final Oauth2Selector oauth2Selector;

    @Transactional
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        final Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        final String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        final Oauth2UserInfo oauth2UserInfo =
                oauth2Selector.getOauth2Mapper(oauth2Provider).toOauth2User(attributes);

        final Member member =
                memberRepository
                        .findByOauth2ProviderAndOauth2Id(
                                oauth2UserInfo.oauth2Provider(), oauth2UserInfo.oauth2Id())
                        .orElseGet(createMember(oauth2UserInfo));

        memberRepository.saveAndFlush(member);

        return new MemberPrincipal(String.valueOf(member.getId()), attributes, member.getRoles());
    }

    private Supplier<Member> createMember(final Oauth2UserInfo oauth2UserInfo) {
        return () ->
                memberMapper.toMember(oauth2UserInfo, List.of(new RoleEntity(Role.ROLE_MEMBER)));
    }
}
