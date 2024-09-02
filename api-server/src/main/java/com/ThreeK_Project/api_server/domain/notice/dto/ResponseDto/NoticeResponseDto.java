package com.ThreeK_Project.api_server.domain.notice.dto.ResponseDto;

import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {
    private UUID noticeId;
    private String title;
    private String content;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public NoticeResponseDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdBy = notice.getCreatedBy().getUsername();
        this.createdAt = notice.getCreatedAt();
    }
}
