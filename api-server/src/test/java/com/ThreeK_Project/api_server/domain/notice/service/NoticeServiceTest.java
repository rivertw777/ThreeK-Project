package com.ThreeK_Project.api_server.domain.notice.service;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeRequestDto;
import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeSearchDto;
import com.ThreeK_Project.api_server.domain.notice.dto.ResponseDto.NoticeResponseDto;
import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import com.ThreeK_Project.api_server.domain.notice.repository.NoticeRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private Notice notice;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    @DisplayName("공지사항 저장 성공 테스트")
    public void createNoticeTest() {
        NoticeRequestDto requestDto = new NoticeRequestDto();

        noticeService.createNotice(requestDto);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    @DisplayName("공지사항 단건 조회 성공 테스트")
    public void getNoticeTest() {
        UUID noticeId = UUID.randomUUID();
        User user = User.createUser(
                "admin", "000000", Role.MANAGER, "00000000000", "address"
        );
        LocalDateTime createdAt = LocalDateTime.now();

        doReturn(noticeId).when(notice).getNoticeId();
        doReturn("제목").when(notice).getTitle();
        doReturn("내용").when(notice).getContent();
        doReturn(user).when(notice).getCreatedBy();
        doReturn(createdAt).when(notice).getCreatedAt();
        doReturn(Optional.of(notice)).when(noticeRepository).findById(noticeId);

        NoticeResponseDto responseDto = noticeService.getNotice(noticeId);
        assertEquals(responseDto.getNoticeId(), noticeId);
        assertEquals(responseDto.getTitle(), "제목");
        assertEquals(responseDto.getContent(), "내용");
        assertEquals(responseDto.getCreatedBy(), user.getUsername());
        assertEquals(responseDto.getCreatedAt(), createdAt);
    }

    @Test
    @DisplayName("공지사항 단건 조회 실패 테스트 - 존재하지 않는 공지사항")
    public void getNoticeTest2() {
        UUID noticeId = UUID.randomUUID();

        doReturn(Optional.empty()).when(noticeRepository).findById(noticeId);

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> noticeService.getNotice(noticeId));
        assertThat(e.getMessage()).isEqualTo("Notice not found");
    }

    @Test
    @DisplayName("공지사항 검색 성공 테스트")
    public void searchNoticeTest() {
        NoticeSearchDto searchDto = new NoticeSearchDto();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "created_at"));
        List<Notice> notices = new ArrayList<>();
        Page<Notice> pages = new PageImpl<>(notices, pageable, 0);

        doReturn(pages)
                .when(noticeRepository)
                .searchNotices(pageable, searchDto);

        Page<NoticeResponseDto> result = noticeService.searchNotices(searchDto);
        assertEquals(result.getTotalElements(), 0);
        verify(noticeRepository, times(1)).searchNotices(pageable, searchDto);
    }

    @Test
    @DisplayName("공지사항 수정 성공 테스트")
    public void updateNoticeTest() {
        UUID noticeId = UUID.randomUUID();
        NoticeRequestDto requestDto = new NoticeRequestDto();

        doReturn(Optional.of(new Notice()))
                .when(noticeRepository)
                .findById(noticeId);

        noticeService.updateNotice(noticeId, requestDto);
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test
    @DisplayName("공지사항 수정 실패 테스트 - 존재하지 않는 공지사항")
    public void updateNoticeTest2() {
        UUID noticeId = UUID.randomUUID();
        NoticeRequestDto requestDto = new NoticeRequestDto();

        doReturn(Optional.empty())
                .when(noticeRepository)
                .findById(noticeId);

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> noticeService.updateNotice(noticeId, requestDto));
        assertThat(e.getMessage()).isEqualTo("Notice not found");
    }

    @Test
    @DisplayName("공지사항 삭제 성공 테스트")
    public void deleteNoticeTest() {
        UUID noticeId = UUID.randomUUID();
        User user = new User();
        Notice notice = new Notice();

        doReturn(Optional.of(notice))
                .when(noticeRepository)
                .findById(noticeId);

        noticeService.deleteNotice(user, noticeId);
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test
    @DisplayName("공지사항 삭제 성공 테스트")
    public void deleteNoticeTest2() {
        UUID noticeId = UUID.randomUUID();
        User user = new User();

        doReturn(Optional.empty())
                .when(noticeRepository)
                .findById(noticeId);

        ApplicationException e = Assertions.
                assertThrows(ApplicationException.class, () -> noticeService.deleteNotice(user, noticeId));
        assertThat(e.getMessage()).isEqualTo("Notice not found");
    }

}