package com.ThreeK_Project.api_server.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionMessage {

    DUPLICATE_NAME("이미 존재하는 이름입니다."),
    USER_NOT_FOUND("해당하는 이름을 가진 사용자가 존재하지 않습니다.");

    private final String value;

}