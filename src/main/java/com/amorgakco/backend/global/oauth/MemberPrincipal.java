package com.amorgakco.backend.global.oauth;

import com.amorgakco.backend.member.domain.Roles;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class MemberPrincipal implements OAuth2User, UserDetails {

    private static final String EMPTY_PASSWORD = "";
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String name;

    public MemberPrincipal(
            final String name, final Map<String, Object> attributes, final List<Roles> roles) {
        this.name = name;
        this.attributes = attributes;
        this.authorities =
                AuthorityUtils.createAuthorityList(
                        roles.stream().map(r -> r.getRole().toString()).toList());
    }

    @Override
    public String getPassword() {
        return EMPTY_PASSWORD;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
