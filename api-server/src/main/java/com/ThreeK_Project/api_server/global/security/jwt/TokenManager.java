package com.ThreeK_Project.api_server.global.security.jwt;

import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private final Key jwtSecretKey;
    private final Long accessTokenExpiration;

    public TokenManager(@Value("${jwt.secret}") String secretKey,
                        @Value("${jwt.access.expiration}") String accessTokenExpiration) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = Long.valueOf(accessTokenExpiration);
    }

    // 토큰 생성
    public String generateToken(UserDetailsCustom userDetails) {
        long now = (new Date()).getTime();
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

}