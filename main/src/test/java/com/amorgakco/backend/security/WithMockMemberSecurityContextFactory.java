package com.amorgakco.backend.security;

import com.amorgakco.backend.global.oauth.MemberPrincipal;
import com.amorgakco.backend.member.domain.Role;
import com.amorgakco.backend.member.domain.Roles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

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
