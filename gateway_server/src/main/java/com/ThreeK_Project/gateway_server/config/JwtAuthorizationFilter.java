package com.ThreeK_Project.gateway_server.config;

import com.ThreeK_Project.gateway_server.user.Role;
import com.ThreeK_Project.gateway_server.user.User;
import com.ThreeK_Project.gateway_server.user.UserCacheRepository;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter {

    private final TokenManager tokenManager;
    private final UserCacheRepository userCacheRepository;

    public Mono<Void> filter(ServerWebExchange exchange) {
        String requestPath = exchange.getRequest().getURI().getPath();
        log.info("요청 API 경로: " + requestPath);

        String token = extractToken(exchange);

        // 인증 없는 API
        if (token == null) {
            return Mono.empty();
        }

        // 인증 필요 API
        if (tokenManager.validateToken(token)) {
            Claims claims = tokenManager.parseClaims(token);
            String username = claims.getSubject();
            User user = userCacheRepository.getUserCache(username);

            // UserCache Null
            if (user == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            List<Role> roles = user.getRoles();

            // 권한 없음
            if (!isAuthorized(requestPath, roles)) {
                return unauthorizedResponse(exchange);
            }

            // 헤더 추가
            exchange.getRequest().mutate()
                    .header("Username", username)
                    .build();
            return Mono.empty();
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isAuthorized(String requestPath, List<Role> roles) {
        if (requestPath.startsWith("/api/master") && !roles.contains(Role.MASTER)) {
            return false;
        }
        if (requestPath.startsWith("/api/admin") && !roles.contains(Role.MANAGER)) {
            return false;
        }
        if (requestPath.startsWith("/api/owner") && !roles.contains(Role.OWNER)) {
            return false;
        }
        return true;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    private void addUsernameHeader(ServerWebExchange exchange, String username) {
        // 새로운 MutableHttpHeaders 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Username", username);

        // 응답의 헤더를 수정 가능한 상태로 만들기
        exchange.getResponse().getHeaders().putAll(headers);
    }

}
