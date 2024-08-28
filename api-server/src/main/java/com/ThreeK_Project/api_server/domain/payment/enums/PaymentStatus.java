package com.ThreeK_Project.api_server.domain.payment.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PaymentStatus {
    WAIT("1", "결제 대기"),
    SUCCESS("2", "결제 실패"),
    FAIL("3", "결제 성공"),
    CANCELED("4", "결제 취소");

    private final String codeValue;
    private final String nameValue;

    PaymentStatus(String codeValue, String nameValue) {
        this.codeValue = codeValue;
        this.nameValue = nameValue;
    }

    public static PaymentStatus getEnum(String codeValue) {
        return Arrays.stream(PaymentStatus.values())
                .filter(t -> t.getCodeValue().equals(codeValue))
                .findAny().orElse(null);
    }
}
