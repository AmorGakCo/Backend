package com.amorgakco.backend.member.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.MemberValidator;
import com.amorgakco.backend.member.domain.SMSNotificationSetting;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private static final String HTTPS_GITHUB_PREFIX = "https://github";
    private static final String GITHUB_PREFIX = "github";
    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public void updateAdditionalInfo(final AdditionalInfoRequest request, final Long memberId) {
        final Member member = getMember(memberId);
        final SMSNotificationSetting smsNotificationSetting =
                SMSNotificationSetting.valueOf(request.smsNotificationSetting());
        member.validateAndUpdateAdditionalInfo(
                request.githubUrl(),
                request.phoneNumber(),
                smsNotificationSetting,
                memberValidator);
    }

    public Member getMember(final Long memberId) {
        return memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(ResourceNotFoundException::memberNotFound);
    }
}
