package com.ThreeK_Project.api_server.domain.user.controller;

import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.REVOKE_ROLE_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.SIGN_UP_SUCCESS;
import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.UPDATE_USER_INFO_SUCCESS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ThreeK_Project.api_server.customMockUser.WithCustomMockUser;
import com.ThreeK_Project.api_server.domain.user.dto.request.AssignRoleRequest;
import com.ThreeK_Project.api_server.domain.user.dto.request.RevokeRoleRequest;
import com.ThreeK_Project.api_server.domain.user.dto.request.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.dto.request.UpdateUserInfoRequest;
import com.ThreeK_Project.api_server.domain.user.dto.response.ManagerUserInfoResponse;
import com.ThreeK_Project.api_server.domain.user.dto.response.UserInfoResponse;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.domain.user.service.UserService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("회원 가입 - 성공 테스트")
    void signUp_Success() throws Exception {
        // Given
        SignUpRequest request = new SignUpRequest("username", "123456", "customer",
                "01012345678", "address");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        SuccessResponse response = new SuccessResponse(SIGN_UP_SUCCESS.getValue());

        when(userService.signUp(request)).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(response.message()));
        verify(userService).signUp(request);
    }

    @Test
    @DisplayName("회원 가입 - 짧은 비밀번호 테스트")
    void signUp_ShortPassword_ThrowsException() throws Exception {
        // Given
        SignUpRequest request = new SignUpRequest("username", "1234", "customer",
                "01012345678", "address");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 - 유효하지 않은 전화번호 테스트")
    void signUp_InvalidPhoneNumber_ThrowsException() throws Exception {
        // Given
        SignUpRequest request = new SignUpRequest("username", "123456", "customer",
                "010", "address");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원 가입 - username 없는 경우 테스트")
    void signUp_NoUsername_ThrowsException() throws Exception {
        // Given
        SignUpRequest request = new SignUpRequest("", "123456", "customer",
                "010", "address");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @WithCustomMockUser
    @Test
    @DisplayName("회원 정보 조회 - 성공 테스트")
    void getUserInfo_Success() throws Exception {
        // Given
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        UserInfoResponse response = new UserInfoResponse(user);

        when(userService.getUserInfo(user)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(response.username()))
                .andExpect(jsonPath("roles").value(response.roles()))
                .andExpect(jsonPath("phoneNumber").value(response.phoneNumber()))
                .andExpect(jsonPath("address").value(response.address()));
        verify(userService).getUserInfo(user);
    }

    @WithCustomMockUser
    @Test
    @DisplayName("회원 정보 수정 - 성공 테스트")
    void updateUserInfo_Success() throws Exception {
        // Given
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        UpdateUserInfoRequest request = new UpdateUserInfoRequest("username2", "1234567",
                "01012345678", "address");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        SuccessResponse response = new SuccessResponse(UPDATE_USER_INFO_SUCCESS.getValue());

        when(userService.updateUserInfo(user, request)).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(response.message()));
        verify(userService).updateUserInfo(user, request);
    }

    @WithCustomMockUser
    @Test
    @DisplayName("회원 탈퇴 - 성공 테스트")
    void deleteUser_Success() throws Exception {
        // Given
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = new SuccessResponse(UPDATE_USER_INFO_SUCCESS.getValue());

        when(userService.deleteUser(user)).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(response.message()));
        verify(userService).deleteUser(user);
    }

    @WithCustomMockUser
    @Test
    @DisplayName("MASTER 권한 부여 - 성공 테스트")
    void assignRoleToUser_Success() throws Exception {
        // Given
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User master = userDetails.getUser();
        String username = "customer";
        AssignRoleRequest request = new AssignRoleRequest("manager");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        SuccessResponse response = new SuccessResponse(UPDATE_USER_INFO_SUCCESS.getValue());

        when(userService.assignRoleToUser(master, username, request)).thenReturn(response);

        // When & Then
        mockMvc.perform(patch("/api/master/users/{username}/roles", username)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(response.message()));
        verify(userService).assignRoleToUser(master, username, request);
    }

    @WithCustomMockUser
    @Test
    @DisplayName("MASTER 권한 회수 - 성공 테스트")
    void revokeRoleFromUser_Success() throws Exception {
        // Given
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User master = userDetails.getUser();
        String username = "manager";
        RevokeRoleRequest request = new RevokeRoleRequest("manager");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        SuccessResponse response = new SuccessResponse(REVOKE_ROLE_SUCCESS.getValue());

        when(userService.revokeRoleFromUser(master, username, request)).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/master/users/{username}/roles", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value(response.message()));
        verify(userService).revokeRoleFromUser(master, username, request);
    }

    @Test
    @DisplayName("MANAGER 회원 조회 - 성공 테스트")
    void getManagerUserInfos_Success() throws Exception {
        // Given
        Pageable pageable = Pageable.ofSize(3);
        User testUser = User.createUser("username", "123456", Role.CUSTOMER,
                "01012345678", "address");
        List<User> testUsers = Arrays.asList(testUser, testUser, testUser);
        Page<User> userPage = new PageImpl<>(testUsers);
        Page<ManagerUserInfoResponse> responses = userPage.map(user -> new ManagerUserInfoResponse(user));

        when(userService.getManagerUserInfos(pageable)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/admin/users")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
        verify(userService).getManagerUserInfos(pageable);
    }

}