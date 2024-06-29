package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.exception.MemberNotFoundException;
import com.amorgakco.backend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProvider jwtProvider;
    private final JwtProperties props;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberJwt createMemberToken(Long memberId) {
        String accessToken = jwtProvider.create(String.valueOf(memberId), props.accessExpiration());
        String refreshToken =
                jwtProvider.create(String.valueOf(memberId), props.refreshExpiration());
        Member member =
                memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        member.updateRefreshToken(refreshToken);
        return new MemberJwt(accessToken, refreshToken);
    }
}
