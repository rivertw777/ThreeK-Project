package com.ThreeK_Project.api_server.domain.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderSortType {
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    private final String value;
}
