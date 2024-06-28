package com.amorgakco.backend.oauth;

import com.amorgakco.backend.member.domain.Account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Oauth2Principal implements OAuth2User {

    public Oauth2Principal(
            final Account account,
            final Map<String, Object> attributes,
            final List<GrantedAuthority> authorities) {
        this.account = account;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    private Account account;
    private Map<String, Object> attributes;
    private List<GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return account.getId().toString();
    }
}
