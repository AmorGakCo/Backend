package com.amorgakco.backend.jwt.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.JwtAuthenticationException;
import com.amorgakco.backend.jwt.dto.MemberTokens;
import com.amorgakco.backend.jwt.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(JwtController.class)
class JwtControllerTest extends RestDocsTest {

    private static final String COOKIE_NAME = "refresh-token";
    private static final String AUTH_HEADER = "Authorization";
    private static final String OLD_REFRESH_TOKEN = "old refresh token";
    private static final String NEW_ACCESS_TOKEN = "new access token";
    private static final String NEW_REFRESH_TOKEN = "new refresh token";

    @MockBean
    JwtService jwtService;
    @MockBean
    JwtCookieLoader jwtCookieLoader;

    @Test
    @DisplayName("새로운 액세스 토큰을 응답받을 수 있다.")
    void reissueAccessToken() throws Exception {
        // given
        final MemberTokens memberTokens = new MemberTokens(NEW_ACCESS_TOKEN, NEW_REFRESH_TOKEN);
        given(jwtService.reissue(OLD_REFRESH_TOKEN)).willReturn(memberTokens);
        final Cookie oldCookie = new Cookie(COOKIE_NAME, OLD_REFRESH_TOKEN);
        // when
        final ResultActions actions = mockMvc.perform(post("/api/tokens", "1").cookie(oldCookie));
        // then
        actions.andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.accessToken").value(NEW_ACCESS_TOKEN));
        // docs
        actions.andDo(print())
            .andDo(document("jwt-reissue", getDocumentRequest(), getDocumentResponse()));
    }

    @Test
    @DisplayName("발급된 토큰 정보와 멤버가 일치하지 않으면 예외를 응답을 받는다.")
    void validateJwtClaim() throws Exception {
        // given
        final Cookie cookie = new Cookie(COOKIE_NAME, OLD_REFRESH_TOKEN);
        // when
        given(jwtService.reissue(OLD_REFRESH_TOKEN))
            .willThrow(JwtAuthenticationException.loginAgain());
        final ResultActions actions = mockMvc.perform(post("/api/tokens", "1").cookie(cookie));
        // then
        actions.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(ErrorCode.LOGIN_AGAIN.getCode()));
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "jwt-reissue-exception",
                    getDocumentRequest(),
                    getDocumentResponse()));
    }

    @Test
    @DisplayName("로그아웃을 수행할 수 있다.")
    void logout() throws Exception {
        // given
        final Cookie cookie = new Cookie(COOKIE_NAME, OLD_REFRESH_TOKEN);
        // when
        final ResultActions actions =
            mockMvc.perform(
                delete("/api/tokens").header(AUTH_HEADER, NEW_ACCESS_TOKEN).cookie(cookie));
        // then
        actions.andExpect(status().isNoContent());
        // docs
        actions.andDo(print())
            .andDo(document("jwt-logout", getDocumentRequest(), getDocumentResponse()));
    }
}
