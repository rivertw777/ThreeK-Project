package com.ThreeK_Project.api_server.domain.user.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSuccessMessage {

    SIGN_UP_SUCCESS("회원가입 완료");

    private final String value;

}