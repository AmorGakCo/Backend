package com.amorgakco.backend.global.jwt.service;

import com.amorgakco.backend.global.jwt.dto.MemberJwt;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.exception.MemberNotFoundException;
import com.amorgakco.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String EMPTY_SUBJECT = "";
    private final JwtCreator jwtCreator;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;

    @Transactional
    public MemberJwt createAndSaveMemberToken(final Long memberId) {
        final Member member =
                memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        final String accessToken =
                jwtCreator.create(String.valueOf(memberId), jwtProperties.accessExpiration());
        final String refreshToken = jwtCreator.create(EMPTY_SUBJECT, jwtProperties.refreshExpiration());
        member.updateRefreshToken(refreshToken);
        return new MemberJwt(accessToken, refreshToken);
    }
}
