package com.ThreeK_Project.api_server.domain.user.service;

import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.DUPLICATE_NAME;
import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.INVALID_ROLE;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.SIGN_UP_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;

import com.ThreeK_Project.api_server.domain.user.dto.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.repository.UserRepository;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest("testUser", "password1234", "customer", "01012345678", "Test Address");
    }

    @Test
    @DisplayName("회원가입 - 성공 테스트")
    void signUp_Success() {
        // When
        SuccessResponse response = userService.signUp(signUpRequest);

        // Then
        verify(passwordEncoder).encode(signUpRequest.password());
        verify(userRepository).save(any(User.class));
        assertEquals(SIGN_UP_SUCCESS.getValue(), response.message());
    }

    @Test
    @DisplayName("회원 가입 - 이름 중복 테스트")
    void signUp_DuplicateUsername_ThrowsException() {
        // Given
        User existingUser = new User();
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(existingUser));

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.signUp(signUpRequest));
        assertEquals(DUPLICATE_NAME.getValue(), exception.getMessage());
    }

    @Test
    @DisplayName("회원 가입 - 유효하지 않은 권한 테스트")
    void signUp_InvalidRole_ThrowsException() {
        // Given
        SignUpRequest testRequest = new SignUpRequest("testUser", "password1234", "user", "01012345678", "Test Address");

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.signUp(testRequest));
        assertEquals(INVALID_ROLE.getValue(), exception.getMessage());
    }

}
