package com.ThreeK_Project.api_server.domain.user.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionMessage {

    TOO_SHORT_PASSWORD("비밀번호는 6자리 이상이어야 합니다."),
    INVALID_PHONE_NUMBER("전화번호 형식이 유효하지 않습니다."),
    DUPLICATE_NAME("이미 존재하는 이름입니다."),
    INVALID_ROLE("유효하지 않은 권한입니다."),
    USER_NOT_FOUND("해당하는 이름을 가진 사용자가 존재하지 않습니다.");

    private final String value;

}