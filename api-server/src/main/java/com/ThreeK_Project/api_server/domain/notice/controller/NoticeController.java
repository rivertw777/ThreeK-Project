package com.ThreeK_Project.api_server.domain.notice.controller;

import com.ThreeK_Project.api_server.domain.notice.dto.ResponseDto.NoticeResponseDto;
import com.ThreeK_Project.api_server.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{noticeId}")
    @Operation(summary = "사용자 공지사항 단건 조회")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable("noticeId") UUID noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }
}
