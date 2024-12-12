package com.amorgakco.backend.notification.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.notification.TestNotificationFactory;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.service.NotificationService;
import com.amorgakco.backend.security.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest extends RestDocsTest {

    @MockBean
    NotificationService notificationService;

    @Test
    @DisplayName("회원의 알림 내역을 조회할 수 있다.")
    @WithMockMember
    void getNotifications() throws Exception {
        // given
        final Long memberId = 1L;
        final Integer page = 0;
        final NotificationMessageResponse response =
            TestNotificationFactory.notificationMessageResponse();
        given(notificationService.getNotifications(memberId, page)).willReturn(response);
        // when
        final ResultActions actions =
            mockMvc.perform(get("/api/notifications").queryParam("page", "0"));
        // then
        actions.andExpect(status().isOk());
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "notifications",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    queryParameters(parameterWithName("page").description("페이지 번호"))));
    }
}
