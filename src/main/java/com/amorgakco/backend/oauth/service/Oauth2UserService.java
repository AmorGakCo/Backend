package com.amorgakco.backend.oauth.service;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.mapper.MemberMapper;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.oauth.UserPrincipal;
import com.amorgakco.backend.oauth.mapper.Oauth2UserInfoMapper;
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
    private final Oauth2UserInfoMapper userInfoMapper;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        Oauth2UserInfo oauth2UserInfo =
                userInfoMapper.toDetailOauth2UserInfo(oauth2Provider, attributes);

        Member member =
                memberRepository
                        .findByProviderAndIdentifier(oauth2Provider, oauth2UserInfo.getIdentifier())
                        .orElseGet(() -> MemberMapper.mapFrom(oauth2Provider, oauth2UserInfo));

        memberRepository.save(member);

        return new UserPrincipal(member, attributes);
    }
}
