package com.ThreeK_Project.api_server.domain.ai.repository;

import com.ThreeK_Project.api_server.domain.ai.entity.AiRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiRequestLogRepository extends JpaRepository<AiRequestLog, UUID> {
}
