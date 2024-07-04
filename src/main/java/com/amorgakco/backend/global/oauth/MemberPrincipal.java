package com.amorgakco.backend.global.oauth;

import com.amorgakco.backend.member.domain.Member;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class MemberPrincipal implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String name;

    public MemberPrincipal(
            final Member member,
            final String name,
            final Map<String, Object> attributes,
            final Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
        this.name = name;
        this.attributes = attributes;
        this.authorities = authorities;
    }
}
