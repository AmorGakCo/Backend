package com.amorgakco.backend.global.oauth.service;

import com.amorgakco.backend.global.oauth.MemberPrincipal;
import com.amorgakco.backend.global.oauth.service.mapper.MemberPrincipalMapper;
import com.amorgakco.backend.global.oauth.service.mapper.Oauth2Mapper;
import com.amorgakco.backend.global.oauth.userinfo.KakaoUserInfo;
import com.amorgakco.backend.global.oauth.userinfo.Oauth2UserInfo;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final Oauth2Mapper oauth2Mapper;
    private final MemberPrincipalMapper memberPrincipalMapper;

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
                                        oauth2Mapper.toMember(
                                                oauth2UserInfo, oauth2Provider, Role.ROLE_MEMBER));

        memberRepository.save(member);

        return new MemberPrincipal(member.getId(),attributes,)
    }
}
