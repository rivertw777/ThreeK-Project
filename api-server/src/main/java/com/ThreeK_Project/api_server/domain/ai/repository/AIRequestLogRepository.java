package com.ThreeK_Project.api_server.domain.ai.repository;

import com.ThreeK_Project.api_server.domain.ai.entity.AIRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AIRequestLogRepository extends JpaRepository<AIRequestLog, UUID> {
}
