package com.ThreeK_Project.api_server.domain.user.dto;

import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.INVALID_PHONE_NUMBER;
import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.TOO_SHORT_PASSWORD;
import static org.junit.jupiter.api.Assertions.*;

import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignUpRequestTest {

    @Test
    @DisplayName("SignUpRequest 생성 - 성공 테스트")
    void createSignUpRequest_Success() {
        SignUpRequest request = new SignUpRequest("username", "123456", "customer",
                "01012345678", "address");
        assertNotNull(request);
    }

    @Test
    @DisplayName("SignUpRequest 생성 - 짧은 비밀번호 테스트")
    void createSignUpRequest_ShortPassword_ThrowsException() {
        Exception exception = assertThrows(ApplicationException.class,
                () -> new SignUpRequest("username", "1234", "customer",
                        "01012345678", "address"));
        assertEquals(TOO_SHORT_PASSWORD.getValue(), exception.getMessage());
    }

    @Test
    @DisplayName("SignUpRequest 생성 - 유효하지 않은 전화번호 테스트")
    void createSignUpRequest_InvalidPhoneNumber_ThrowsException() {
        Exception exception = assertThrows(ApplicationException.class,
                () -> new SignUpRequest("username", "123456", "user",
                        "010", "address"));
        assertEquals(INVALID_PHONE_NUMBER.getValue(), exception.getMessage());
    }

}
