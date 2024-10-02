package com.amorgakco.backend.participant.controller;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.participant.TestParticipantFactory;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;
import com.amorgakco.backend.participant.dto.TardinessRequest;
import com.amorgakco.backend.participant.dto.TemperatureResponse;
import com.amorgakco.backend.participant.service.ParticipantService;
import com.amorgakco.backend.security.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipantController.class)
class ParticipantControllerTest extends RestDocsTest {

    @MockBean
    private ParticipantService participantService;

    @Test
    @DisplayName("회원의 그룹 참여 내역을 조회할 수 있다.")
    @WithMockMember
    void memberParticipationHistory() throws Exception {
        // given
        final Long memberId = 1L;
        final Integer page = 0;
        final ParticipationHistoryResponse participationHistoryResponse =
                TestParticipantFactory.participationHistoryResponse();
        given(participantService.getParticipationHistory(memberId, page))
                .willReturn(participationHistoryResponse);
        // when
        final ResultActions actions =
                mockMvc.perform(get("/participants/histories").queryParam("page", "0"));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "participation-history",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                queryParameters(parameterWithName("page").description("페이지 번호"))));
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

    @Test
    @DisplayName("참여자는 모각코를 탈퇴할 수 있다.")
    @WithMockMember
    void withdraw() throws Exception {
        // when
        final ResultActions actions =
                mockMvc.perform(
                        delete("/participants/groups/{groupId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(document(
                        "participant-withdraw",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupId").description("그룹ID")))
                );
    }

    @Test
    @DisplayName("참여자는 지각 요청을 할 수 있다.")
    @WithMockMember
    void tardy() throws Exception {
        final TardinessRequest tardinessRequest = new TardinessRequest(10);
        // when
        final ResultActions actions =
                mockMvc.perform(
                        post("/participants/groups/{groupId}/tardiness", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(tardinessRequest)));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(document(
                        "participant-tardiness",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("groupId").description("그룹ID")))
                );
    }

    @Test
    @DisplayName("참여자의 모각코 온도를 올릴 수 있다.")
    @WithMockMember
    void increaseTemperature() throws Exception {
        //given
        final Long groupId = 1L;
        final Long targetMemberId = 2L;
        final Long requestMemberId = 1L;
        final TemperatureResponse temperatureResponse = new TemperatureResponse(1);
        given(participantService.increaseTemperature(groupId, requestMemberId, targetMemberId)).willReturn(temperatureResponse);
        // when
        final ResultActions actions =
                mockMvc.perform(
                        patch("/participants/{targetMemberId}/groups/{groupId}/temperature-increase", targetMemberId, groupId)
                                .contentType(MediaType.APPLICATION_JSON));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(document(
                        "participant-increase-temperature",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(parameterWithName("targetMemberId").description("온도를 상승시킬 참여자"),
                                parameterWithName("groupId").description("그룹ID")))
                );
    }

    @Test
    @DisplayName("참여자의 모각코 온도를 내릴 수 있다.")
    @WithMockMember
    void decreaseTemperature() throws Exception {
        //given
        final Long groupId = 1L;
        final Long targetMemberId = 2L;
        final Long requestMemberId = 1L;
        final TemperatureResponse temperatureResponse = new TemperatureResponse(-1);
        given(participantService.increaseTemperature(groupId, requestMemberId, targetMemberId)).willReturn(temperatureResponse);
        // when
        final ResultActions actions =
                mockMvc.perform(
                        patch("/participants/{targetMemberId}/groups/{groupId}/temperature-decrease", 2L, 1L)
                                .contentType(MediaType.APPLICATION_JSON));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(document(
                        "participant-decrease-temperature",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("targetMemberId").description("온도를 상승시킬 참여자 ID"),
                                parameterWithName("groupId").description("그룹ID")))
                );
    }
}
