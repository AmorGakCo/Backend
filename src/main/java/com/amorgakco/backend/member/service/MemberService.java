package com.amorgakco.backend.member.service;

import com.amorgakco.backend.global.GoogleS2Const;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.dto.PrivateMemberResponse;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.member.service.mapper.MemberMapper;
import com.amorgakco.backend.oauth2.provider.Oauth2Member;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public Long updateOrSave(final Oauth2Member oauth2Member) {
        return memberRepository
                .findByOauth2ProviderAndOauth2Id(
                        oauth2Member.oauth2ProviderType(), oauth2Member.oauth2Id())
                .map(
                        existingMember -> {
                            existingMember.updateNicknameAndImgUrl(
                                    oauth2Member.nickname(), oauth2Member.imgUrl());
                            return existingMember.getId();
                        })
                .orElseGet(() -> createMember(oauth2Member));
    }

    private Long createMember(final Oauth2Member oauth2Member) {
        final Member newMember = memberMapper.toMember(oauth2Member);
        return memberRepository.save(newMember).getId();
    }

    @Transactional
    public void updateAdditionalInfo(final AdditionalInfoRequest request, final Long memberId) {
        final Member member = getMember(memberId);
        final String memberCellToken =
                createMemberCellToken(request.latitude(), request.longitude());
        member.validateAndUpdateAdditionalInfo(
                request.githubUrl(),
                request.phoneNumber(),
                request.smsNotificationSetting(),
                memberCellToken);
    }

    public Member getMember(final Long memberId) {
        return memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(ResourceNotFoundException::memberNotFound);
    }

    private String createMemberCellToken(final double latitude, final double longitude) {
        final S2Point point = S2LatLng.fromDegrees(latitude, longitude).toPoint();
        return S2CellId.fromPoint(point).parent(GoogleS2Const.S2_CELL_LEVEL.getValue()).toToken();
    }

    public PrivateMemberResponse getPrivateMember(final Long memberId) {
        final Member member = getMember(memberId);
        return memberMapper.toPrivateMemberResponse(member);
    }
}
