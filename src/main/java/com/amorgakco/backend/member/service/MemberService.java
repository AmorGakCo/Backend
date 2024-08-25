package com.amorgakco.backend.member.service;

import com.amorgakco.backend.global.GoogleS2Const;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.jwt.dto.MemberAccessToken;
import com.amorgakco.backend.jwt.service.JwtService;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.dto.LoginResponse;
import com.amorgakco.backend.member.dto.PrivateMemberResponse;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.member.service.mapper.MemberMapper;
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
    private final JwtService jwtService;

    public LoginResponse login(final String refreshToken) {
        final MemberAccessToken memberAccessToken =
                jwtService.createMemberAccessToken(refreshToken);
        final Member member = getMember(Long.parseLong(memberAccessToken.memberId()));
        return memberMapper.toLoginResponse(member, memberAccessToken.accessToken());
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

    private String createMemberCellToken(final double latitude, final double longitude) {
        final S2Point point = S2LatLng.fromDegrees(latitude, longitude).toPoint();
        return S2CellId.fromPoint(point).parent(GoogleS2Const.S2_CELL_LEVEL.getValue()).toToken();
    }

    public Member getMember(final Long memberId) {
        return memberRepository
                .findByIdWithRoles(memberId)
                .orElseThrow(ResourceNotFoundException::memberNotFound);
    }

    public PrivateMemberResponse getPrivateMember(final Long memberId) {
        final Member member = getMember(memberId);
        return memberMapper.toPrivateMemberResponse(member);
    }
}
