package com.ThreeK_Project.api_server.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionMessage {

    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN("토큰 검증 실패");

    private final String value;

}