package com.ThreeK_Project.api_server.domain.auth.service;

import static com.ThreeK_Project.api_server.domain.auth.message.AuthExceptionMessage.PASSWORD_NOT_MATCH;
import static org.junit.jupiter.api.Assertions.*;

import com.ThreeK_Project.api_server.domain.auth.dto.LoginRequest;
import com.ThreeK_Project.api_server.domain.auth.dto.LoginResponse;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsServiceCustom;
import com.ThreeK_Project.api_server.global.security.jwt.TokenManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsServiceCustom userDetailsServiceCustom;

    @Mock
    private TokenManager tokenManager;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("testUser", "password1234");
    }

    @Test
    @DisplayName("회원 로그인 - 성공 테스트")
    void login_Success() {
        // Given
        User user = User.createUser("testUser", "password1234", Role.CUSTOMER, "01012345678", "Test Address");
        UserDetailsCustom userDetails = new UserDetailsCustom(user);
        when(userDetailsServiceCustom.loadUserByUsername(loginRequest.username())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(tokenManager.generateToken(userDetails)).thenReturn("accessToken");

        // When
        LoginResponse response = authService.login(loginRequest);

        // Then
        verify(userDetailsServiceCustom).loadUserByUsername(loginRequest.username());
        verify(passwordEncoder).matches(loginRequest.password(), user.getPassword());
        verify(tokenManager).generateToken(userDetails);
        assertEquals("accessToken", response.token());
    }

    @Test
    @DisplayName("회원 로그인 - 유효하지 않은 비밀번호 테스트")
    void login_InvalidPassword_ThrowsException() {
        // Given
        User user = User.createUser("testUser", "password1234", Role.CUSTOMER, "01012345678", "Test Address");
        UserDetailsCustom userDetails = new UserDetailsCustom(user);
        when(userDetailsServiceCustom.loadUserByUsername(loginRequest.username())).thenReturn(userDetails);
        LoginRequest testRequest = new LoginRequest("testUser", "password1235");

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> authService.login(testRequest));
        assertEquals(PASSWORD_NOT_MATCH.getValue(), exception.getMessage());
    }

}
