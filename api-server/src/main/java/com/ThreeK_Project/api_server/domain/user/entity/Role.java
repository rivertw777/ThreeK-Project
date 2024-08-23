package com.ThreeK_Project.api_server.domain.user.entity;

import com.ThreeK_Project.api_server.global.exception.ApplicationException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    CUSTOMER("CUSTOMER"),
    OWNER("OWNER"),
    MANAGER("MANAGER"),
    MASTER("MASTER");

    private final String value;

    public static Role fromValue(String value) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ApplicationException("Invalid role: " + value));
    }

}
