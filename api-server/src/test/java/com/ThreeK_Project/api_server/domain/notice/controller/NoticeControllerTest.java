package com.ThreeK_Project.api_server.domain.notice.controller;

import com.ThreeK_Project.api_server.domain.notice.dto.ResponseDto.NoticeResponseDto;
import com.ThreeK_Project.api_server.domain.notice.service.NoticeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class NoticeControllerTest {

    @Mock
    private NoticeService noticeService;

    @InjectMocks
    private NoticeController noticeController;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(noticeController).build();
    }

    @Test
    @DisplayName("주문 단건 조회 성공 테스트")
    public void getNoticeTest() throws Exception {
        UUID noticeId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        NoticeResponseDto responseDto = new NoticeResponseDto(
                noticeId, "제목", "내용", "admin", createdAt
        );

        doReturn(responseDto)
                .when(noticeService)
                .getNotice(noticeId);

        mockMvc.perform(get("/api/notices/" + noticeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("noticeId").value(noticeId.toString()))
                .andExpect(jsonPath("title").value("제목"))
                .andExpect(jsonPath("content").value("내용"))
                .andExpect(jsonPath("createdBy").value("admin"))
                .andExpect(jsonPath("createdAt")
                        .value(createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

}