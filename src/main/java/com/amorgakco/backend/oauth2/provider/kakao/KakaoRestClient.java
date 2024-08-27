package com.amorgakco.backend.oauth2.provider.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoRestClient {

    @PostExchange(
            url = "https://kauth.kakao.com/oauth/token",
            contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoAuthorization getKakaoAccessToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoMemberResponse getKakaoMember(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String bearerToken);
}
