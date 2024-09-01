package com.ThreeK_Project.api_server.domain.center_report.service;

import com.ThreeK_Project.api_server.domain.center_report.dto.RequestDto.CenterReportRequestDto;
import com.ThreeK_Project.api_server.domain.center_report.entity.CenterReport;
import com.ThreeK_Project.api_server.domain.center_report.repository.CenterReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CenterReportServiceTest {

    @Mock
    private CenterReportRepository centerReportRepository;

    @InjectMocks
    private CenterReportService centerReportService;

    @Test
    @DisplayName("고객센터 신고 생성 성공 테스트")
    public void createCenterReportTest() {
        CenterReportRequestDto requestDto = new CenterReportRequestDto();

        centerReportService.createCenterReport(requestDto);
        verify(centerReportRepository, times(1)).save(any(CenterReport.class));
    }

}