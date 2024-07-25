package com.amorgakco.backend.member.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static com.amorgakco.backend.fixture.AccessToken.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.service.MemberService;
import com.amorgakco.backend.security.WithMockMember;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends RestDocsTest {

    @MockBean MemberService memberService;

    @Test
    @DisplayName("깃헙아이디,전화번호,SMS수신여부를 설정할 수 있다")
    @WithMockMember
    void saveOrUpdateAdditionalInfo() throws Exception {
        // given
        final AdditionalInfoRequest request =
                AdditionalInfoRequest.builder()
                        .githubUrl("https://github/songhaechan")
                        .phoneNumber("01012341234")
                        .smsNotificationSetting("off")
                        .build();

        final ResultActions actions =
                // when
                mockMvc.perform(
                                patch("/members")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toRequestBody(request)))
                        // then
                        .andExpect(status().isNoContent());
        // docs
        actions.andDo(print())
                .andDo(
                        document(
                                "member-save-additional-info",
                                getDocumentRequest(),
                                getDocumentResponse()));
    }
}
