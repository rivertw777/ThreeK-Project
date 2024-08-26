package com.ThreeK_Project.api_server.domain.auth.service;

import static com.ThreeK_Project.api_server.domain.auth.message.AuthExceptionMessage.PASSWORD_NOT_MATCH;

import com.ThreeK_Project.api_server.domain.auth.dto.LoginRequest;
import com.ThreeK_Project.api_server.domain.auth.dto.LoginResponse;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsServiceCustom;
import com.ThreeK_Project.api_server.global.security.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final TokenManager tokenManager;

    // 로그인
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest requestParam) {
        UserDetailsCustom userDetails = userDetailsServiceCustom.loadUserByUsername(requestParam.username());

        validatePassword(requestParam.password(), userDetails.getUser().getPassword());

        String token = tokenManager.generateToken(userDetails);
        return new LoginResponse(token);
    }

    // 비밀번호 검증
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ApplicationException(PASSWORD_NOT_MATCH.getValue());
        }
    }

}
