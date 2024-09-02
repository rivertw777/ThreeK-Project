package com.ThreeK_Project.api_server.domain.notice.controller;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.service.NoticeService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PutMapping("/{noticeId}")
    @Operation(summary = "관리자 공지사항 수정")
    public ResponseEntity<SuccessResponse> updateNotice(
            @PathVariable("noticeId") UUID noticeId,
            @RequestBody @Valid NoticeRequestDto requestDto
    ) {
        noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(new SuccessResponse("공지사항 수정 성공"));
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "관리자 공지사항 삭제")
    public ResponseEntity<SuccessResponse> deleteNotice(@PathVariable("noticeId") UUID noticeId) {
        UserDetailsCustom detailsCustom = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        noticeService.deleteNotice(detailsCustom.getUser(), noticeId);
        return ResponseEntity.ok(new SuccessResponse("공지사항 삭제 성공"));
    }

}
