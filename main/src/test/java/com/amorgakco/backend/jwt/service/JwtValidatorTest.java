package com.amorgakco.backend.jwt.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.amorgakco.backend.fixture.security.TestSecretKey;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.JwtAuthenticationException;
import com.amorgakco.backend.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class JwtValidatorTest {

    private static final String ERROR_CODE = "errorCode";
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final JwtValidator jwtValidator =
            new JwtValidator(memberRepository, TestSecretKey.create());
    private final JwtCreator jwtCreator = new JwtCreator(TestSecretKey.create());

    @Test
    @DisplayName("토큰 claim에 일치하지 않는 멤버는 예외를 발생시킨다.")
    void invalidTokenClaim() {
        // given
        final String accessToken = jwtCreator.create("1", 50000L);
        given(memberRepository.findByIdWithRoles(1L)).willReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> jwtValidator.getAuthentication(accessToken))
                .isInstanceOf(JwtAuthenticationException.class)
                .hasFieldOrPropertyWithValue(ERROR_CODE, ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("액세스 토큰이 만료되면 예외를 발생시킨다.")
    void accessTokenExpired() {
        // given
        final String accessToken = jwtCreator.create("1", 0L);
        // when & then
        assertThatThrownBy(() -> jwtValidator.getAuthentication(accessToken))
                .isInstanceOf(JwtAuthenticationException.class)
                .hasFieldOrPropertyWithValue(ERROR_CODE, ErrorCode.ACCESS_TOKEN_EXPIRED);
    }

    @Test
    @DisplayName("액세스 토큰에서 Claim을 추출할 수 있다.")
    void getClaim() {
        // given
        final String memberId = "1";
        final String accessToken = jwtCreator.create(memberId, 5000L);
        // when
        final String claim = jwtValidator.validateAndGetClaim(accessToken);
        // then
        assertThat(claim).isEqualTo(memberId);
    }

    @Test
    @DisplayName("리프레쉬 토큰과 저장된 회원의 ID와 요청한 회원의 ID가 다르면 true를 반환한다.")
    void validateMemberIdFail() {
        // given
        final String accessMemberId = "1";
        final String refreshMemberId = "2";
        // when
        final boolean result = jwtValidator.areBothNotEqual(accessMemberId, refreshMemberId);
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("리프레쉬 토큰과 저장된 회원의 ID와 요청한 회원의 ID가 같다면 false를 반환한다.")
    void validateMemberIdSuccess() {
        // given
        final String accessMemberId = "1";
        final String refreshMemberId = "1";
        // when
        final boolean result = jwtValidator.areBothNotEqual(accessMemberId, refreshMemberId);
        // then
        assertThat(result).isFalse();
    }
}
