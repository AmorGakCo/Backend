package com.amorgakco.backend.global.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.fixture.security.TestSecretKey;
import com.amorgakco.backend.global.oauth.MemberPrincipal;
import com.amorgakco.backend.jwt.service.JwtCreator;
import com.amorgakco.backend.jwt.service.JwtExtractor;
import com.amorgakco.backend.jwt.service.JwtValidator;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

class JwtAuthenticationFilterTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final JwtAuthenticationFilter jwtAuthenticationFilter =
            new JwtAuthenticationFilter(
                    new JwtValidator(memberRepository, TestSecretKey.create()), new JwtExtractor());
    private final JwtCreator jwtCreator = new JwtCreator(TestSecretKey.create());

    @Test
    @DisplayName("올바른 Jwt토큰은 Security Context에 Authentication을 설정할 수 있다.")
    void setAuthentication() throws ServletException, IOException {
        // given
        final Member member = TestMemberFactory.create(1L);
        given(memberRepository.findByIdWithRoles(1L)).willReturn(Optional.of(member));
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final String token = jwtCreator.create("1", 4000L);
        request.addHeader("Authorization", "Bearer " + token);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, new DummyFilterChain());
        // then
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        final MemberPrincipal principal = (MemberPrincipal) authentication.getPrincipal();
        assertThat(principal.getName()).isEqualTo("1");
    }
}
