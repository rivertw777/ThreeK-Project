package com.ThreeK_Project.gateway_server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiPrefix {

    MASTER("/api/master"),
    ADMIN("/api/admin"),
    OWNER("/api/owner");

    private final String value;

}
