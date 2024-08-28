package com.ThreeK_Project.api_server.domain.auth.service;

import static com.ThreeK_Project.api_server.domain.auth.message.AuthExceptionMessage.USER_NOT_ACTIVE;
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

    private User user;

    private UserDetailsCustom userDetails;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        String encodedPassword = passwordEncoder.encode("123456");
        user = User.createUser("username", encodedPassword, Role.CUSTOMER, "01012345678", "address");
        userDetails = new UserDetailsCustom(user);
        loginRequest = new LoginRequest("username", "123456");
    }

    @Test
    @DisplayName("회원 로그인 - 성공 테스트")
    void login_Success() {
        // Given
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
    @DisplayName("회원 로그인 - 탈퇴 회원 테스트")
    void login_NotActiveUser_ThrowsException() {
        // Given
        user.deleteUser(user);
        when(userDetailsServiceCustom.loadUserByUsername(loginRequest.username())).thenReturn(userDetails);
        LoginRequest testRequest = new LoginRequest("username", "123456");

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> authService.login(testRequest));
        assertEquals(USER_NOT_ACTIVE.getValue(), exception.getMessage());
    }

}
