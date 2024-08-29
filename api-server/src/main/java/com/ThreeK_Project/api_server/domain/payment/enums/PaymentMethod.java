package com.ThreeK_Project.api_server.domain.payment.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PaymentMethod {
    CARD("1", "카드");

    private final String codeValue;
    private final String nameValue;

    PaymentMethod(String codeValue, String nameValue) {
        this.codeValue = codeValue;
        this.nameValue = nameValue;
    }

    public static PaymentMethod getEnum(String codeValue) {
        return Arrays.stream(PaymentMethod.values())
                .filter(t -> t.getCodeValue().equals(codeValue))
                .findAny().orElse(null);
    }
}
