package com.ThreeK_Project.api_server.domain.auth.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionMessage {

    USER_NOT_ACTIVE("이미 탈퇴한 회원입니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.");

    private final String value;

}