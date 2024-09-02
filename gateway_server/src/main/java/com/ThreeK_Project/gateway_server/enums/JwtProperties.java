package com.ThreeK_Project.gateway_server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtProperties {

    HEADER_STRING("Authorization"),
    TOKEN_PREFIX("Bearer ");

    private final String value;

}
