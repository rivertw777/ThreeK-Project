package com.ThreeK_Project.api_server.domain.user.dto;

import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.INVALID_PHONE_NUMBER;
import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.TOO_SHORT_PASSWORD;

import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import java.util.regex.Pattern;

public record SignUpRequest(String username, String password, String role, String phoneNumber,
                            String address) {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\d{10,15}$");

    public SignUpRequest {
        if (password.length() < 6) {
            throw new ApplicationException(TOO_SHORT_PASSWORD.getValue());
        }
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new ApplicationException(INVALID_PHONE_NUMBER.getValue());
        }
    }

}
