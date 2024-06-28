package com.amorgakco.backend.oauth.service;

import com.amorgakco.backend.member.domain.Account;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.mapper.AccountMapper;
import com.amorgakco.backend.member.repository.AccountRepository;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.oauth.Oauth2Principal;
import com.amorgakco.backend.oauth.exception.Oauth2UserNotFound;
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
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final Oauth2UserInfoMapper userInfoMapper;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        String oauth2Provider = userRequest.getClientRegistration().getRegistrationId();
        Oauth2UserInfo oauth2UserInfo =
                userInfoMapper.toDetailOauth2UserInfo(oauth2Provider, attributes);

        if (isNotRegistered(oauth2Provider, oauth2UserInfo.getIdentifier())) {
            Member member = memberRepository.save(new Member());
            accountRepository.save(accountMapper.toAccount(member, oauth2Provider, oauth2UserInfo));
        } else {
            updateAccount(oauth2Provider, oauth2UserInfo);
        }
        return new Oauth2Principal();
    }

    private boolean isNotRegistered(String registrationId, String identifier) {
        return accountRepository.existsByProviderAndIdentifier(registrationId, identifier);
    }

    private void updateAccount(final String provider, final Oauth2UserInfo oauth2UserInfo) {
        Account account =
                accountRepository
                        .findByProviderAndIdentifier(provider, oauth2UserInfo.getIdentifier())
                        .orElseThrow(Oauth2UserNotFound::new);
        account.updateAccount(
                oauth2UserInfo.getIdentifier(),
                oauth2UserInfo.getImgUrl(),
                oauth2UserInfo.getNickname());
    }
}
