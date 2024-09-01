package com.ThreeK_Project.api_server.domain.center_report.controller;

import com.ThreeK_Project.api_server.domain.center_report.dto.RequestDto.CenterReportRequestDto;
import com.ThreeK_Project.api_server.domain.center_report.entity.CenterReport;
import com.ThreeK_Project.api_server.domain.center_report.service.CenterReportService;
import com.ThreeK_Project.api_server.domain.notice.controller.NoticeController;
import com.ThreeK_Project.api_server.domain.notice.service.NoticeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class CenterReportControllerTest {

    @Mock
    private CenterReportService centerReportService;

    @InjectMocks
    private CenterReportController centerReportController;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(centerReportController).build();
    }

    @Test
    @DisplayName("고객센터 신고 생성 성공 테스트")
    public void CenterReportCreateTest() throws Exception {
        String content = "{\"title\":\"title\",\"content\":\"content\"}";

        mockMvc.perform(post("/api/center-report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("고객센터 신고 저장 성공"));
        verify(centerReportService, times(1))
                .createCenterReport(any(CenterReportRequestDto.class));
    }
}