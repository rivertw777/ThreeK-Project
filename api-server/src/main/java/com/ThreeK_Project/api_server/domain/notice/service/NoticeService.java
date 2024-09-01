package com.ThreeK_Project.api_server.domain.notice.service;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeSearchDto;
import com.ThreeK_Project.api_server.domain.notice.dto.ResponseDto.NoticeResponseDto;
import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import com.ThreeK_Project.api_server.domain.notice.repository.NoticeRepository;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    // 공지사항 단건 조회
    public NoticeResponseDto getNotice(UUID noticeId) {
        return new NoticeResponseDto(findNoticeById(noticeId));
    }

    // 공지사항 검색
    public Page<NoticeResponseDto> searchNotices(NoticeSearchDto searchDto) {
        Sort sort = searchDto.getAscending() ? Sort.by(Sort.Direction.ASC, searchDto.getSortBy().getValue())
                : Sort.by(Sort.Direction.DESC, searchDto.getSortBy().getValue());

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);

        return noticeRepository.searchNotices(pageable, searchDto).map(NoticeResponseDto::new);
    }

    public Notice findNoticeById(UUID noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException("Notice not found"));
    }
}
