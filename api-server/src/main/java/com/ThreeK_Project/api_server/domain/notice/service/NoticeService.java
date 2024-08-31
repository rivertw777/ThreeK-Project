package com.ThreeK_Project.api_server.domain.notice.service;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import com.ThreeK_Project.api_server.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 공지사항 저장
    @Transactional
    public void createNotice(NoticeRequestDto requestDto) {
        noticeRepository.save(Notice.createNotice(
                requestDto.getTitle(), requestDto.getContent()
        ));
    }

}
