package com.amorgakco.backend.participant.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.fixture.participant.TestParticipantFactory;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;
import com.amorgakco.backend.participant.service.ParticipantService;
import com.amorgakco.backend.security.WithMockMember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ParticipantController.class)
class ParticipantControllerTest extends RestDocsTest {

    @MockBean private ParticipantService participantService;

    @Test
    @DisplayName("회원의 그룹 참여 내역을 조회할 수 있다.")
    @WithMockMember
    void getMemberPrivate() throws Exception {
        // given
        final Member member = TestMemberFactory.create(1L);
        final ParticipationHistoryResponse participationHistoryResponse =
                TestParticipantFactory.participationHistoryResponse();
        given(participantService.getParticipationHistory(member, 0))
                .willReturn(participationHistoryResponse);
        // when
        final ResultActions actions =
                mockMvc.perform(get("/participants/history").queryParam("page", "0"));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "participation-history",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    @DisplayName("회원의 위치를 인증할 수 있다.")
    @WithMockMember
    void verifyLocation() throws Exception {
        // given
        final LocationVerificationRequest locationVerificationRequest =
                TestParticipantFactory.locationVerificationRequest();
        // when
        final ResultActions actions =
                mockMvc.perform(
                        patch("/participants/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(locationVerificationRequest)));
        // then
        actions.andExpect(status().isNoContent());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "participation-location-verification",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }
}
