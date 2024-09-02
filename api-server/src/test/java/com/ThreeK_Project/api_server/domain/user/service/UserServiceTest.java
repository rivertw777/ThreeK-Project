package com.ThreeK_Project.api_server.domain.user.service;

import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.DUPLICATE_NAME;
import static com.ThreeK_Project.api_server.domain.user.message.UserExceptionMessage.INVALID_ROLE;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.ASSIGN_ROLE_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.DELETE_USER_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.REVOKE_ROLE_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.SIGN_UP_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.UPDATE_USER_INFO_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;

import com.ThreeK_Project.api_server.domain.user.dto.request.AssignRoleRequest;
import com.ThreeK_Project.api_server.domain.user.dto.request.RevokeRoleRequest;
import com.ThreeK_Project.api_server.domain.user.dto.request.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.dto.request.UpdateUserInfoRequest;
import com.ThreeK_Project.api_server.domain.user.dto.response.ManagerUserInfoResponse;
import com.ThreeK_Project.api_server.domain.user.dto.response.UserInfoResponse;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.domain.user.repository.UserRepository;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    private User user;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest("username", "123456", "customer",
                "01012345678", "address");
        user = User.createUser("username", "123456", Role.CUSTOMER,
                "01012345678", "address");
    }

    @Test
    @DisplayName("회원 가입 - 성공 테스트")
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
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUser));

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.signUp(signUpRequest));
        assertEquals(DUPLICATE_NAME.getValue(), exception.getMessage());
    }

    @Test
    @DisplayName("회원 가입 - 유효하지 않은 권한 테스트")
    void signUp_InvalidRole_ThrowsException() {
        // Given
        SignUpRequest testRequest = new SignUpRequest("username", "123456", "user",
                "01012345678", "address");

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> userService.signUp(testRequest));
        assertEquals(INVALID_ROLE.getValue(), exception.getMessage());
    }

    @Test
    @DisplayName("회원 정보 조회 - 성공 테스트")
    void getUserInfo_Success() {
        // When
        UserInfoResponse response = userService.getUserInfo(user);

        // Then
        assertEquals("username", response.username());
        assertEquals("01012345678", response.phoneNumber());
        assertEquals("address", response.address());
        List<String> roles = Collections.singletonList(Role.CUSTOMER.getValue());
        assertEquals(roles, response.roles());
    }

    @Test
    @DisplayName("회원 정보 수정 - 성공 테스트")
    void updateUserInfo_Success() {
        // Given
        UpdateUserInfoRequest request = new UpdateUserInfoRequest("username2", "1234567",
                "01012345678", "address");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // When
        SuccessResponse response = userService.updateUserInfo(user, request);

        // Then
        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).delete(user);
        verify(userRepository).save(any(User.class));
        assertEquals(UPDATE_USER_INFO_SUCCESS.getValue(), response.message());
    }

    @Test
    @DisplayName("회원 탈퇴 - 성공 테스트")
    void deleteUser_Success() {
        // Given
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // When
        SuccessResponse response = userService.deleteUser(user);

        // Then
        verify(userRepository).findByUsername(user.getUsername());
        assertEquals(DELETE_USER_SUCCESS.getValue(), response.message());
    }

    @Test
    @DisplayName("MASTER 권한 부여 - 성공 테스트")
    void assignRoleToUser_Success() {
        // Given
        AssignRoleRequest request = new AssignRoleRequest("manager");
        User customer = User.createUser("customer", "123456", Role.CUSTOMER,
                "01012345678", "address");
        when(userRepository.findByUsername(customer.getUsername())).thenReturn(Optional.of(customer));

        // When
        SuccessResponse response = userService.assignRoleToUser(user, customer.getUsername(), request);

        // Then
        verify(userRepository).findByUsername(customer.getUsername());
        List<Role> roles = new ArrayList<>(Arrays.asList(Role.CUSTOMER, Role.MANAGER));
        assertEquals(roles, customer.getRoles());
        assertEquals(ASSIGN_ROLE_SUCCESS.getValue(), response.message());
    }

    @Test
    @DisplayName("MASTER 권한 회수 - 성공 테스트")
    void revokeRoleFromUser_Success() {
        // Given
        RevokeRoleRequest request = new RevokeRoleRequest("manager");
        User manager = User.createUser("customer", "123456", Role.CUSTOMER,
                "01012345678", "address");
        manager.addRole(Role.MANAGER, user);
        when(userRepository.findByUsername(manager.getUsername())).thenReturn(Optional.of(manager));

        // When
        SuccessResponse response = userService.revokeRoleFromUser(user, manager.getUsername(), request);

        // Then
        verify(userRepository).findByUsername(manager.getUsername());
        List<Role> roles = new ArrayList<>(List.of(Role.CUSTOMER));
        assertEquals(roles, manager.getRoles());
        assertEquals(REVOKE_ROLE_SUCCESS.getValue(), response.message());
    }

    @Test
    @DisplayName("MANAGER 회원 조회 - 성공 테스트")
    void getManagerUserInfos_Success() {
        // Given
        List<User> users = Arrays.asList(user, user, user);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = Pageable.ofSize(3);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // When
        Page<ManagerUserInfoResponse> responses = userService.getManagerUserInfos(pageable);

        // Then
        verify(userRepository).findAll(pageable);
        assertEquals(3, responses.getTotalElements());
        assertEquals("username", responses.getContent().get(0).username());
    }

}