package com.ThreeK_Project.api_server.global.security.config;

import com.ThreeK_Project.api_server.global.security.auth.UserDetailsServiceCustom;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilterCustom extends OncePerRequestFilter {

    private final String HEADER_NAME = "X-User-Name";

    private final UserDetailsServiceCustom userDetailsServiceCustom;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 헤더 확인
        String username = request.getHeader(HEADER_NAME);
        if (username == null) {
            chain.doFilter(request, response);
            return;
        }

        // 인증 객체 반환
        Authentication authentication = userDetailsServiceCustom.getAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User {} Authenticated", username);
        chain.doFilter(request, response);
    }

}
