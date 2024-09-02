package com.ThreeK_Project.api_server.domain.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeSortType {
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String value;
}
