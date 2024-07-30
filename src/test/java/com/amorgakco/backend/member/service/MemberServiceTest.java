package com.amorgakco.backend.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.SmsNotificationSetting;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.Optional;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final MemberService memberService =
            new MemberService(memberRepository, geometryFactory);

    @Test
    @DisplayName("멤버의 추가정보를 입력할 수 있다.")
    void updateOrSaveAdditionalInfo() {
        // given
        final Member member = TestMemberFactory.create(1L);
        given(memberRepository.findByIdWithRoles(1L)).willReturn(Optional.of(member));
        final AdditionalInfoRequest request = TestMemberFactory.createAdditionalInfoRequest("on");
        // when
        memberService.updateAdditionalInfo(request, 1L);
        // then
        assertThat(member.getGithubUrl()).isEqualTo(request.githubUrl());
        assertThat(member.getSmsNotificationSetting()).isEqualTo(SmsNotificationSetting.ON);
        assertThat(member.getPhoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(member.getPoint().getX()).isEqualTo(request.longitude());
        assertThat(member.getPoint().getY()).isEqualTo(request.latitude());
    }
}
