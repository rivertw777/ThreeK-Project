package com.ThreeK_Project.api_server.domain.center_report.repository;

import com.ThreeK_Project.api_server.domain.center_report.entity.CenterReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CenterReportRepository extends JpaRepository<CenterReport, UUID> {
}
