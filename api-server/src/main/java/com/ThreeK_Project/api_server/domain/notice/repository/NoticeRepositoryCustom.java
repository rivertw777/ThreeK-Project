package com.ThreeK_Project.api_server.domain.notice.repository;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeSearchDto;
import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    public Page<Notice> searchNotices(Pageable pageable, NoticeSearchDto searchDto);

}
