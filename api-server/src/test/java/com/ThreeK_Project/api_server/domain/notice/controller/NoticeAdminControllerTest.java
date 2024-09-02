package com.ThreeK_Project.api_server.domain.notice.controller;

import com.ThreeK_Project.api_server.customMockUser.WithCustomMockUser;
import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.service.NoticeService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class NoticeAdminControllerTest {
    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeAdminController noticeAdminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeAdminController).build();
    }

    @Test
    @DisplayName("공지사항 저장 성공 테스트")
    public void createNoticeTest() throws Exception {
        String content = "{\"title\": \"제목\", \"content\": \"내용\"}";

        mockMvc.perform(post("/api/admin/notices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("공지사항 저장 성공"));

        verify(noticeService, times(1))
                .createNotice(any(NoticeRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 수정 성공 테스트")
    public void updateNoticeTest() throws Exception {
        UUID noticeId = UUID.randomUUID();
        String content = "{\"title\": \"제목\", \"content\": \"내용\"}";

        mockMvc.perform(put("/api/admin/notices/" + noticeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("공지사항 수정 성공"));

        verify(noticeService, times(1))
                .updateNotice(eq(noticeId), any(NoticeRequestDto.class));
    }

    @Test
    @DisplayName("공지사항 삭제 성공 테스트")
    @WithCustomMockUser
    public void deleteNoticeTest() throws Exception {
        UUID noticeId = UUID.randomUUID();
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        mockMvc.perform(delete("/api/admin/notices/" + noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("공지사항 삭제 성공"));

        verify(noticeService, times(1))
                .deleteNotice(eq(user), eq(noticeId));
    }

}