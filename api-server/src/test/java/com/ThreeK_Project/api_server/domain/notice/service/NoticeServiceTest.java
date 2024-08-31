package com.ThreeK_Project.api_server.domain.notice.service;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import com.ThreeK_Project.api_server.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 저장 성공 테스트")
    public void createNoticeTest() {
        NoticeRequestDto requestDto = new NoticeRequestDto();

        noticeService.createNotice(requestDto);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

}