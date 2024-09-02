package com.ThreeK_Project.api_server.domain.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentSortType {
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at"),;

    private final String value;
}
