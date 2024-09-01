package com.ThreeK_Project.api_server.domain.center_report.service;

import com.ThreeK_Project.api_server.domain.center_report.dto.RequestDto.CenterReportRequestDto;
import com.ThreeK_Project.api_server.domain.center_report.entity.CenterReport;
import com.ThreeK_Project.api_server.domain.center_report.repository.CenterReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CenterReportService {

    private final CenterReportRepository centerReportRepository;

    @Transactional
    public void createCenterReport(CenterReportRequestDto requestDto) {
        CenterReport centerReport = CenterReport.createCenterReport(
                requestDto.getTitle(), requestDto.getContent()
        );
        centerReportRepository.save(centerReport);
    }
}
