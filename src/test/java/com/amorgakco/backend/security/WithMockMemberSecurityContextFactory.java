package com.amorgakco.backend.security;

import com.amorgakco.backend.global.oauth.MemberPrincipal;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.member.domain.Roles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WithMockMemberSecurityContextFactory
        implements WithSecurityContextFactory<WithMockMember> {

    @Override
    public SecurityContext createSecurityContext(final WithMockMember annotation) {
        final Map<String, Object> attributes = new HashMap<>();
        final MemberPrincipal principal =
                new MemberPrincipal(
                        annotation.value(), attributes, List.of(new Roles(Role.ROLE_MEMBER)));
        final Authentication authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        principal, principal.getPassword(), principal.getAuthorities());
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
