package com.ThreeK_Project.api_server.global.security.config;

import static com.ThreeK_Project.api_server.global.security.jwt.JwtProperties.HEADER_STRING;
import static com.ThreeK_Project.api_server.global.security.jwt.JwtProperties.TOKEN_PREFIX;

import com.ThreeK_Project.api_server.global.security.auth.UserDetailsServiceCustom;
import com.ThreeK_Project.api_server.global.security.jwt.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 헤더 토큰 확인
        String header = request.getHeader(HEADER_STRING.getValue());
        if (header == null || !header.startsWith(TOKEN_PREFIX.getValue())) {
            chain.doFilter(request, response);
            return;
        }

        // 헤더 토큰 추출
        String token = request.getHeader(HEADER_STRING.getValue()).replace(TOKEN_PREFIX.getValue(), "");

        // 토큰 검증 및 인가
        if (tokenManager.validateToken(token)) {
                Authentication authentication = userDetailsServiceCustom.extractAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

}
