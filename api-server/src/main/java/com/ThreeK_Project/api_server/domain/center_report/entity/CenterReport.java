package com.ThreeK_Project.api_server.domain.center_report.entity;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_center_reports")
public class CenterReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reportId;
    private String reportStatus;
    private String title;
    private String content;
    private String response;

    @OneToOne(fetch = FetchType.LAZY)
    private User respondent;

    public static CenterReport createCenterReport(
            String title, String content
    ) {
        return CenterReport.builder()
                .reportStatus("wait")
                .title(title)
                .content(content)
                .build();
    }

}
