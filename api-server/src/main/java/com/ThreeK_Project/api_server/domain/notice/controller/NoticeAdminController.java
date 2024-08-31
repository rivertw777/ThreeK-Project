package com.ThreeK_Project.api_server.domain.notice.controller;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.service.NoticeService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notices")
public class NoticeAdminController {

    private final NoticeService noticeService;

    @PostMapping
    @Operation(summary = "관리자 공지사항 저장")
    public ResponseEntity<SuccessResponse> createNotice(@RequestBody @Valid NoticeRequestDto requestDto) {
        noticeService.createNotice(requestDto);
        return ResponseEntity.ok(new SuccessResponse("공지사항 저장 성공"));
    }

}
