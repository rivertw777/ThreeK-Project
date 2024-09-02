package com.ThreeK_Project.api_server.domain.user.dto.request;

import com.ThreeK_Project.api_server.domain.user.message.UserInfoExceptionMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = UserInfoExceptionMessage.USERNAME_REQUIRED)
        String username,

        @NotBlank(message = UserInfoExceptionMessage.PASSWORD_REQUIRED)
        @Size(min = 6, message = UserInfoExceptionMessage.TOO_SHORT_PASSWORD)
        String password,

        @NotBlank(message = UserInfoExceptionMessage.ROLE_REQUIRED)
        String role,

        @NotBlank(message = UserInfoExceptionMessage.PHONE_NUMBER_REQUIRED)
        @Pattern(regexp = "^\\d{10,15}$", message = UserInfoExceptionMessage.INVALID_PHONE_NUMBER)
        String phoneNumber,

        @NotBlank(message = UserInfoExceptionMessage.ADDRESS_REQUIRED)
        String address
) {
}
