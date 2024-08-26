package com.ThreeK_Project.api_server.domain.auth.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionMessage {

    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.");

    private final String value;

}