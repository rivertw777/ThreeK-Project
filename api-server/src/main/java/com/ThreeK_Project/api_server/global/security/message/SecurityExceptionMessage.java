package com.ThreeK_Project.api_server.global.security.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityExceptionMessage {

    INVALID_TOKEN("토큰 검증이 실패하였습니다."),
    UNAUTHORIZED("인증이 필요합니다. 로그인해주세요."),
    NO_AUTHORITY("권한이 없습니다.");

    private final String value;

}