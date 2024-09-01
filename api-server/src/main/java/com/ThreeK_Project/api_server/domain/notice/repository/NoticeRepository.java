package com.ThreeK_Project.api_server.domain.notice.repository;

import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID>, NoticeRepositoryCustom {

}
