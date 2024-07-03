package com.amorgakco.backend.oauth.service;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.oauth.MemberPrincipal;
import com.amorgakco.backend.oauth.service.mapper.Oauth2Mapper;
import com.amorgakco.backend.oauth.userinfo.KakaoUserInfo;
import com.amorgakco.backend.oauth.userinfo.Oauth2UserInfo;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        final Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        final String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        final Oauth2UserInfo oauth2UserInfo = new KakaoUserInfo(attributes);

        final Member member =
                memberRepository
                        .findByProviderAndIdentifier(oauth2Provider, oauth2UserInfo.getOauth2Id())
                        .orElseGet(
                                () ->
                                        Oauth2Mapper.INSTANCE.toMember(
                                                oauth2UserInfo, oauth2Provider, Role.ROLE_MEMBER));

        memberRepository.save(member);

        return new MemberPrincipal(member, attributes);
    }
}
