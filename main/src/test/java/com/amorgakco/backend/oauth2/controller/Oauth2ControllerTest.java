package com.amorgakco.backend.oauth2.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.config.KakaoRedirectionLoginUrl;
import com.amorgakco.backend.global.config.LocalKakaoRedirectionLoginUrl;
import com.amorgakco.backend.jwt.controller.JwtCookieLoader;
import com.amorgakco.backend.jwt.dto.MemberTokens;
import com.amorgakco.backend.jwt.service.JwtService;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.oauth2.dto.Oauth2MemberResponse;
import com.amorgakco.backend.oauth2.service.Oauth2Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(Oauth2Controller.class)
class Oauth2ControllerTest extends RestDocsTest {

    @MockBean
    Oauth2Service oauth2Service;
    @MockBean
    JwtService jwtService;
    @MockBean
    JwtCookieLoader jwtCookieLoader;
    @MockBean
    LocalKakaoRedirectionLoginUrl localKakaoRedirectionLoginUrl;
    @MockBean
    KakaoRedirectionLoginUrl kakaoRedirectionLoginUrl;

    @Test
    @DisplayName("소셜로그인 redirect url을 받을 수 있다.")
    void redirectUrl() throws Exception {
        // given
        given(localKakaoRedirectionLoginUrl.redirectionUrl())
            .willReturn("http://localhost:3000/redirected/kakao");
        // when
        final ResultActions actions = mockMvc.perform(get("/api/oauth2/{oauth2ProviderType}", "kakao")
            .header("Referer", "http://localhost:3000"));
        // then
        actions.andExpect(status().isFound());
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "oauth2-redirect",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("oauth2ProviderType")
                            .description("소셜 로그인 타입"))));
    }

    @Test
    @DisplayName("소셜로그인 후 토큰을 응답받을 수 있다.")
    void login() throws Exception {
        // given
        final String authCode = "jeofijaowejfowf";
        final String memberId = "1";
        final String redirectUrl = "http://localhost:3000/redirected/kakao";
        final Oauth2MemberResponse oauth2MemberResponse =
            TestMemberFactory.oauth2MemberResponse(memberId);
        final MemberTokens memberTokens = new MemberTokens("access-token", "refresh-token");
        given(oauth2Service.login(Oauth2ProviderType.KAKAO, authCode,redirectUrl))
            .willReturn(oauth2MemberResponse);
        given(jwtService.createAndSaveMemberTokens(memberId)).willReturn(memberTokens);
        // when
        final ResultActions actions =
            mockMvc.perform(
                post("/api/oauth2/{oauth2ProviderType}", "kakao")
                    .header("Referer", "http://localhost:3000")
                    .queryParam("authCode", authCode));
        // then
        actions.andExpect(status().isCreated());
        // docs
        actions.andDo(print())
            .andDo(
                document(
                    "oauth2-login",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("oauth2ProviderType")
                            .description("소셜 로그인 타입")),
                    queryParameters(
                        parameterWithName("authCode")
                            .description("소셜 로그인 Authorization Code"))));
    }
}
