package com.ThreeK_Project.api_server.global.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtProperties {

    HEADER_STRING("Authorization"),
    TOKEN_PREFIX("Bearer ");

    private final String value;

}
