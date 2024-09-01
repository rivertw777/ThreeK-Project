package com.ThreeK_Project.api_server.domain.center_report.controller;

import com.ThreeK_Project.api_server.domain.center_report.dto.RequestDto.CenterReportRequestDto;
import com.ThreeK_Project.api_server.domain.center_report.entity.CenterReport;
import com.ThreeK_Project.api_server.domain.center_report.service.CenterReportService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/center-report")
public class CenterReportController {

    private final CenterReportService centerReportService;

    @PostMapping
    @Operation(summary = "사용자 고객센터 신고 생성")
    public ResponseEntity<SuccessResponse> createCenterReport(@RequestBody final CenterReportRequestDto requestDto) {
        centerReportService.createCenterReport(requestDto);
        return ResponseEntity.ok(new SuccessResponse("고객센터 신고 저장 성공"));
    }
}
