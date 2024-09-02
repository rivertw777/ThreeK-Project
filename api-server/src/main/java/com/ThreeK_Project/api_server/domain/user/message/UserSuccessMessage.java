package com.ThreeK_Project.api_server.domain.user.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSuccessMessage {

    SIGN_UP_SUCCESS("회원가입 완료"),
    UPDATE_USER_INFO_SUCCESS("회원 정보 수정 완료"),
    DELETE_USER_SUCCESS("회원 탈퇴 완료"),
    ASSIGN_ROLE_SUCCESS("권한 부여 완료"),
    REVOKE_ROLE_SUCCESS("권한 회수 완료");

    private final String value;

}