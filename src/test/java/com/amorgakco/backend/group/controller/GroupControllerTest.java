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
import com.amorgakco.backend.global.IdResponse;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.IllegalTimeException;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.security.WithMockMember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(GroupController.class)
@WithMockMember
class GroupControllerTest extends RestDocsTest {

    @MockBean GroupService groupService;

    @Test
    @DisplayName("그룹을 생성 후 생성된 그룹 ID를 응답 받을 수 있다.")
    void registerGroup() throws Exception {
        // given
        final GroupRegisterRequest request = createGroupRegisterRequest().build();
        given(groupService.register(request, 1L)).willReturn(new IdResponse(1L));
        // when
        final ResultActions actions =
                mockMvc.perform(
                                post("/groups")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request)))
                        // then
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.data.id").value("1"));
        // docs
        actions.andDo(print())
                .andDo(document("group-register", getDocumentRequest(), getDocumentResponse()));
    }

    private GroupRegisterRequest.GroupRegisterRequestBuilder createGroupRegisterRequest() {
        return GroupRegisterRequest.builder()
                .groupCapacity(3)
                .address("seoul")
                .longitude(126.9769117)
                .latitude(37.572389)
                .name("amorgakco")
                .description("coding")
                .beginAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(3));
    }

    @Test
    @DisplayName("최대 모임지속 시간을 넘으면 예외를 응답을 받을 수 있다.")
    void validateMaxDuration() throws Exception {
        // given
        final GroupRegisterRequest request =
                createGroupRegisterRequest()
                        .beginAt(LocalDateTime.now())
                        .endAt(LocalDateTime.now().plusHours(8))
                        .build();
        given(groupService.register(request, 1L)).willThrow(IllegalTimeException.maxDuration());
        // when
        final ResultActions actions =
                mockMvc.perform(
                                post("/groups")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request)))
                        // then
                        .andExpect(status().isBadRequest())
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
        final GroupRegisterRequest request =
                createGroupRegisterRequest()
                        .beginAt(LocalDateTime.now())
                        .endAt(LocalDateTime.now().plusMinutes(30))
                        .build();
        given(groupService.register(request, 1L)).willThrow(IllegalTimeException.minDuration());
        // when
        final ResultActions actions =
                mockMvc.perform(
                                post("/groups")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request)))
                        // then
                        .andExpect(status().isBadRequest())
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
    @DisplayName("모임 종료 시간이 모임 시작 시간보다 빠르면 예외를 응답을 수 있다.")
    void validateBeginAndEndTime() throws Exception {
        // given
        final GroupRegisterRequest request =
                createGroupRegisterRequest()
                        .beginAt(LocalDateTime.now())
                        .endAt(LocalDateTime.now().minusHours(3))
                        .build();
        given(groupService.register(request, 1L))
                .willThrow(IllegalTimeException.startTimeAfterEndTime());
        // when
        final ResultActions actions =
                mockMvc.perform(
                                post("/groups")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request)))
                        // then
                        .andExpect(status().isBadRequest())
                        .andExpect(
                                jsonPath("$.code")
                                        .value(ErrorCode.START_TIME_AFTER_ENT_TIME.getCode()));
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-register-time-exception",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }

    @Test
    @DisplayName("그룹 단건 조회 응답을 받을 수 있다.")
    void getBasicGroup() throws Exception {
        // given
        final GroupBasicResponse response = createGroupBasicResponse().build();
        given(groupService.getBasicGroup(1L)).willReturn(response);
        // when
        final ResultActions actions =
                mockMvc.perform(get("/groups/basic/{groupId}", 1L))
                        // then
                        .andExpect(status().isOk());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "group-get-basic",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(parameterWithName("groupId").description("그룹 ID"))));
    }

    private GroupBasicResponse.GroupBasicResponseBuilder createGroupBasicResponse() {
        return GroupBasicResponse.builder()
                .hostNickname("nickname")
                .groupCapacity(3)
                .address("seoul")
                .hostImgUrl("http://fakeImgUrl")
                .currentParticipants(2)
                .hostPoint(100)
                .beginAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusHours(3));
    }

    @Test
    @DisplayName("그룹 삭제 응답을 받을 수 있다.")
    void deleteGroup() throws Exception {
        // when
        final ResultActions actions =
                mockMvc.perform(delete("/groups/{groupId}", 1L))
                        // then
                        .andExpect(status().isNoContent());
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
        final ResultActions actions =
                mockMvc.perform(delete("/groups/{groupId}", 1L))
                        // then
                        .andExpect(status().isBadRequest());
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
        final GroupSearchResponse response = createGroupSearchResponse().build();
        given(groupService.getNearByGroups(126.9769117, 37.572389, 500)).willReturn(response);
        final ResultActions actions =
                mockMvc.perform(
                                get("/groups/locations")
                                        .queryParam("longitude", "126.9769117")
                                        .queryParam("latitude", "37.572389")
                                        .queryParam("radius", "500"))
                        // then
                        .andExpect(status().isOk());
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

    private GroupSearchResponse.GroupSearchResponseBuilder createGroupSearchResponse() {
        return GroupSearchResponse.builder()
                .locations(
                        List.of(
                                new GroupLocation(23.1111, 122.2222, 1L),
                                new GroupLocation(12.1341, 123.2222, 2L),
                                new GroupLocation(11.1111, 221.2222, 3L)));
    }
}
