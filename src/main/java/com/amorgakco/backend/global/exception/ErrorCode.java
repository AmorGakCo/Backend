package com.amorgakco.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    ACCESS_TOKEN_EXPIRED("0001", "액세스 토큰이 만료됐습니다."),
    REFRESH_TOKEN_REQUIRED("0002", "리프레쉬 토큰을 쿠키에서 찾을 수 없습니다."),
    TOKEN_CLAIM_NOT_MATCHED("0003", "액세스 토큰과 리프레쉬 토큰의 정보가 다릅니다."),
    CANNOT_PARSE_TOKEN("0004", "식별할 수 없는 액세스토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND("0005", "리프레쉬 토큰을 찾을 수 없습니다."),
    ACCESS_TOKEN_NOT_FOUND("0006", "헤더에서 액세스 토큰을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("0007", "찾을 수 없는 회원입니다."),
    RECHECK_YOUR_TOKEN("0008", "액세스 토큰과 리프레쉬 토큰을 다시 확인바랍니다.");

    private final String code;
    private final String message;
}
