package com.ThreeK_Project.api_server.domain.notice.entity;

import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_notices")
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID noticeId;
    private String title;
    private String content;

    public static Notice createNotice(String title, String content) {
        return Notice.builder()
                .title(title)
                .content(content)
                .build();
    }

}
