package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.IdResponse;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.GroupAuthorityException;
import com.amorgakco.backend.global.exception.IllegalTimeException;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
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

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
@WithMockMember
class GroupControllerTest extends RestDocsTest {

    @MockBean
    GroupService groupService;

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
        Member member = TestMemberFactory.create(1L);
        final Long groupId = 1L;
        given(groupService.getBasicGroup(groupId,member)).willReturn(response);
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
        final Long memberId = 1L;
        given(groupService.getDetailGroup(groupId,memberId)).willReturn(response);
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
        doThrow(GroupAuthorityException.noAuthorityForGroup()).when(groupService).delete(1L, 1L);
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
}
