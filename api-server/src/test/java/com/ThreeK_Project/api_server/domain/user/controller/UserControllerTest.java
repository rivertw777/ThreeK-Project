package com.ThreeK_Project.api_server.domain.user.controller;

import static com.ThreeK_Project.api_server.domain.user.message.UserSuccessMessage.SIGN_UP_SUCCESS;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ThreeK_Project.api_server.customMockUser.WithCustomMockUser;
import com.ThreeK_Project.api_server.domain.user.dto.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.service.UserService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("회원 가입 - 성공 테스트")
    void signUp_Success() throws Exception {
        // Given
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
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
    }

    @WithCustomMockUser
    @Test
    @DisplayName("사용자 정보 조회 - 성공 테스트")
    void getUserInfo_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}