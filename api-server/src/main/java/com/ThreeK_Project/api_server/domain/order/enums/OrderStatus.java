package com.ThreeK_Project.api_server.domain.order.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderStatus {
    WAIT("1", "주문 접수 대기"),
    CANCELED("2", "주문 취소"),
    RECEIPT("3", "주문 접수"),
    COMPLETE("4", "배달 완료");

    private final String codeValue;
    private final String nameValue;

    OrderStatus(String codeValue, String nameValue) {
        this.codeValue = codeValue;
        this.nameValue = nameValue;
    }

    public static OrderStatus getEnum(String codeValue) {
        return Arrays.stream(OrderStatus.values())
                .filter(t -> t.getCodeValue().equals(codeValue))
                .findAny().orElse(null);
    }
}
