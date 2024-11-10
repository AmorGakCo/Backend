package com.amorgakco.backend.fcmtoken.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fcmtoken.dto.FcmTokenSaveRequest;
import com.amorgakco.backend.fcmtoken.service.FcmTokenService;
import com.amorgakco.backend.security.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


@WebMvcTest(FcmTokenController.class)
class FcmTokenControllerTest extends RestDocsTest {

    @MockBean
    private FcmTokenService fcmTokenService;

    @Test
    @WithMockMember
    @DisplayName("FCM 토큰을 저장한다.")
    void saveFcmToken() throws Exception {
        //given
        final FcmTokenSaveRequest fcmTokenSaveRequest = new FcmTokenSaveRequest("fcm-token");
        // when
        final ResultActions actions =
            mockMvc.perform(
                post("/fcm-tokens")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toRequestBody(fcmTokenSaveRequest)));
        // then
        actions.andExpect(status().isCreated());
        // docs
        actions.andDo(print())
            .andDo(document(
                "fcm-token-save",
                getDocumentRequest(),
                getDocumentResponse()));
    }
}