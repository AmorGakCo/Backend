package com.amorgakco.backend.member.service;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.exception.MemberNotFoundException;
import com.amorgakco.backend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMember(final Long memberId) {
        return memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
