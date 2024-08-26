package com.ThreeK_Project.api_server.domain.order.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderType {
    ONLINE("1", "이용 대기"),
    FACE_TO_FACE("2", "예약 취소");

    private final String codeValue;
    private final String nameValue;

    OrderType(String codeValue, String nameValue) {
        this.codeValue = codeValue;
        this.nameValue = nameValue;
    }

    public static OrderType getEnum(String codeValue) {
        return Arrays.stream(OrderType.values())
                .filter(t -> t.getCodeValue().equals(codeValue))
                .findAny().orElse(null);
    }
}
