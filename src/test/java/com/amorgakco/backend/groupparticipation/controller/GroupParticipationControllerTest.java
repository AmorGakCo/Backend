package com.amorgakco.backend.groupparticipation.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.groupparticipation.service.GroupParticipationService;
import com.amorgakco.backend.security.WithMockMember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(GroupParticipationController.class)
class GroupParticipationControllerTest extends RestDocsTest {

    @MockBean private GroupParticipationService groupParticipationService;

    @Test
    @DisplayName("그룹에 참여요청을 보낼 수 있다.")
    @WithMockMember
    void getMemberPrivate() throws Exception {
        // when
        final ResultActions actions = mockMvc.perform(post("/groups/{groupId}/participation", 1L));
        // then
        actions.andExpect(status().isNoContent());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "groups-participation",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(parameterWithName("groupId").description("그룹 ID"))));
    }
}
