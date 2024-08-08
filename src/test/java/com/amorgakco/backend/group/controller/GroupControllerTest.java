package com.amorgakco.backend.group.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.IdResponse;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.IllegalTimeException;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.security.WithMockMember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

@WebMvcTest(GroupController.class)
@WithMockMember
class GroupControllerTest extends RestDocsTest {

    @MockBean GroupService groupService;

    @Test
    @DisplayName("그룹을 생성 후 생성된 그룹 ID를 응답 받을 수 있다.")
    void registerGroup() throws Exception {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.plusHours(3);
        final GroupRegisterRequest request = TestGroupFactory.groupRegisterRequest(beginAt, endAt);
        final Member host = TestMemberFactory.create(1L);
        given(memberService.getMember(1L)).willReturn(host);
        given(groupService.register(request, host)).willReturn(new IdResponse(1L));
        // when
        final ResultActions actions =
                mockMvc.perform(
                        post("/groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(request)));
        // then
        actions.andExpect(status().isCreated()).andExpect(jsonPath("$.data.id").value("1"));
        // docs
        actions.andDo(print())
                .andDo(document("group-register", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("최대 모임지속 시간을 넘으면 예외를 응답을 받을 수 있다.")
    void validateMaxDuration() throws Exception {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.plusHours(8);
        final GroupRegisterRequest request = TestGroupFactory.groupRegisterRequest(beginAt, endAt);
        final Member host = TestMemberFactory.create(1L);
        given(memberService.getMember(1L)).willReturn(host);
        given(groupService.register(request, host)).willThrow(IllegalTimeException.maxDuration());
        // when
        final ResultActions actions =
                mockMvc.perform(
                        post("/groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(request)));
        // then
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MAX_DURATION.getCode()));
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-register-max-duration-exception",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    @DisplayName("최소 모임지속 시간을 넘기지 않으면 예외를 응답을 받을 수 있다.")
    void validateMinDuration() throws Exception {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.plusMinutes(30);
        final GroupRegisterRequest request = TestGroupFactory.groupRegisterRequest(beginAt, endAt);
        final Member host = TestMemberFactory.create(1L);
        given(memberService.getMember(1L)).willReturn(host);
        given(groupService.register(request, host)).willThrow(IllegalTimeException.minDuration());
        // when
        final ResultActions actions =
                mockMvc.perform(
                        post("/groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(request)));
        // then
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.MIN_DURATION.getCode()));
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-register-min-duration-exception",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    @DisplayName("모임 종료 시간이 모임 시작 시간보다 빠르면 예외를 응답한다.")
    void validateBeginAndEndTime() throws Exception {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.minusHours(3);
        final GroupRegisterRequest request = TestGroupFactory.groupRegisterRequest(beginAt, endAt);
        final Member host = TestMemberFactory.create(1L);
        given(memberService.getMember(1L)).willReturn(host);
        given(groupService.register(request, host))
                .willThrow(IllegalTimeException.startTimeAfterEndTime());
        // when
        final ResultActions actions =
                mockMvc.perform(
                        post("/groups")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toRequestBody(request)));
        // then
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorCode.START_TIME_AFTER_ENT_TIME.getCode()));
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-register-time-exception",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    @DisplayName("그룹 기본 단건 조회 응답을 받을 수 있다.")
    void getBasicGroup() throws Exception {
        // given
        final GroupBasicResponse response = TestGroupFactory.groupBasicResponse();
        final Long groupId = 1L;
        given(groupService.getBasicGroup(groupId)).willReturn(response);
        // when
        final ResultActions actions = mockMvc.perform(get("/groups/basic/{groupId}", 1L));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-get-basic",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(parameterWithName("groupId").description("그룹 ID"))));
    }

    @Test
    @DisplayName("그룹 상세 단건 조회 응답을 받을 수 있다.")
    void getDetailGroup() throws Exception {
        // given
        final GroupDetailResponse response = TestGroupFactory.groupDetailResponse();
        final Long groupId = 1L;
        given(groupService.getDetailGroup(groupId)).willReturn(response);
        // when
        final ResultActions actions = mockMvc.perform(get("/groups/detail/{groupId}", 1L));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-get-detail",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(parameterWithName("groupId").description("그룹 ID"))));
    }

    @Test
    @DisplayName("그룹 삭제 응답을 받을 수 있다.")
    void deleteGroup() throws Exception {
        // when
        final ResultActions actions = mockMvc.perform(delete("/groups/{groupId}", 1L));
        // then
        actions.andExpect(status().isNoContent());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-delete",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(parameterWithName("groupId").description("그룹 ID"))));
    }

    @Test
    @DisplayName("그룹 삭제는 호스트가 아니라면 예외 응답을 받을 수 있다.")
    void validateGroupHostDeletion() throws Exception {
        // when
        doThrow(IllegalAccessException.noAuthorityForGroup()).when(groupService).delete(1L, 1L);
        final ResultActions actions = mockMvc.perform(delete("/groups/{groupId}", 1L));
        // then
        actions.andExpect(status().isBadRequest());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-delete-host-exception",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(parameterWithName("groupId").description("그룹 ID"))));
    }

    @Test
    @DisplayName("그룹들의 위치 정보를 응답 받을 수 있다.")
    void getNearByGroups() throws Exception {
        // when
        final GroupSearchResponse response = TestGroupFactory.groupSearchResponse();
        final double longitude = 126.9769117;
        final double latitude = 37.572389;
        final double radius = 500;
        given(groupService.getNearByGroups(longitude, latitude, radius)).willReturn(response);
        final ResultActions actions =
                mockMvc.perform(
                        get("/groups/locations")
                                .queryParam("longitude", "126.9769117")
                                .queryParam("latitude", "37.572389")
                                .queryParam("radius", "500"));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-get-nearby",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                queryParameters(
                                        parameterWithName("longitude").description("경도"),
                                        parameterWithName("latitude").description("위도"),
                                        parameterWithName("radius").description("반지름 [단위:m]"))));
    }
}
