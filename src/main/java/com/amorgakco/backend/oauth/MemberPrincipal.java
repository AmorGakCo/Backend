package com.amorgakco.backend.oauth;

import com.amorgakco.backend.member.domain.Member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class MemberPrincipal implements OAuth2User {

    public MemberPrincipal(final Member member, final Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    private final Member member;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString()));
    }

    @Override
    public String getName() {
        return member.getId().toString();
    }
}
