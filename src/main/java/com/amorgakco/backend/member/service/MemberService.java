package com.amorgakco.backend.member.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.SmsNotificationSetting;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final GeometryFactory geometryFactory;

    @Transactional
    public void updateAdditionalInfo(final AdditionalInfoRequest request, final Long memberId) {
        final Member member = getMember(memberId);
        final SmsNotificationSetting smsNotificationSetting =
                SmsNotificationSetting.valueOf(request.smsNotificationSetting());
        final Point point =
                geometryFactory.createPoint(
                        new Coordinate(request.longitude(), request.latitude()));
        member.validateAndUpdateAdditionalInfo(
                request.githubUrl(), request.phoneNumber(), smsNotificationSetting, point);
    }

    public Member getMember(final Long memberId) {
        return memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(ResourceNotFoundException::memberNotFound);
    }
}
