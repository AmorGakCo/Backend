package com.amorgakco.backend.groupapplication.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.groupapplication.service.GroupApplicationService;
import com.amorgakco.backend.security.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(GroupApplicationController.class)
class GroupApplicationControllerTest extends RestDocsTest {

    @MockBean
    private GroupApplicationService groupApplicationService;

    @Test
    @DisplayName("그룹에 참여요청을 보낼 수 있다.")
    @WithMockMember
    void getMemberPrivate() throws Exception {
        // when
        final ResultActions actions = mockMvc.perform(post("/groups/{groupId}/participation", 1L));
        // then
        actions.andExpect(status().isCreated());
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "groups-participation",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("groupId").description("참여할 그룹 ID"))));
    }

    @Test
    @DisplayName("그룹 참여를 허가할 수 있다.")
    @WithMockMember
    void approveParticipation() throws Exception {
        // when
        final ResultActions actions =
            mockMvc.perform(post("/groups/{groupId}/participation/{memberId}", 1L, 2L));
        // then
        actions.andExpect(status().isCreated());
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "groups-participation-approve",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("groupId").description("참여할 그룹 ID"),
                        parameterWithName("memberId").description("참여 요청 회원 ID"))));
    }

    @Test
    @DisplayName("그룹 참여를 거절할 수 있다.")
    @WithMockMember
    void rejectParticipation() throws Exception {
        // when
        final ResultActions actions =
            mockMvc.perform(patch("/groups/{groupId}/participation/{memberId}", 1L, 2L));
        // then
        actions.andExpect(status().isNoContent());
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "groups-participation-reject",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("groupId").description("참여할 그룹 ID"),
                        parameterWithName("memberId").description("참여 요청 회원 ID"))));
    }
}
