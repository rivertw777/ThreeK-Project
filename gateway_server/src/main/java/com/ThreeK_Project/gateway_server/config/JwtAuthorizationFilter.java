package com.ThreeK_Project.gateway_server.config;

import static com.ThreeK_Project.gateway_server.enums.JwtProperties.HEADER_STRING;
import static com.ThreeK_Project.gateway_server.enums.JwtProperties.TOKEN_PREFIX;

import com.ThreeK_Project.gateway_server.enums.ApiPrefix;
import com.ThreeK_Project.gateway_server.enums.ErrorMessage;
import com.ThreeK_Project.gateway_server.user.Role;
import com.ThreeK_Project.gateway_server.user.User;
import com.ThreeK_Project.gateway_server.user.UserCacheRepository;
import com.ThreeK_Project.gateway_server.utils.ResponseWriter;
import com.ThreeK_Project.gateway_server.utils.TokenManager;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter {

    private final String HEADER_NAME = "X-User-Name";

    private final TokenManager tokenManager;
    private final UserCacheRepository userCacheRepository;
    private final ResponseWriter responseWriter;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();
        log.info("요청 API 경로: " + requestPath);

        String token = extractToken(exchange);
        try {
            // 토큰 확인
            if (token == null) {
                return unauthorizedResponse(exchange, ErrorMessage.TOKEN_NOT_FOUND);
            }

            // 토큰 검증
            if (tokenManager.validateToken(token)) {
                Claims claims = tokenManager.parseClaims(token);
                String username = claims.getSubject();
                User user = userCacheRepository.getUserCache(username);

                // UserCache Null
                if (user == null) {
                    return unauthorizedResponse(exchange, ErrorMessage.USER_NOT_FOUND);
                }
                List<Role> roles = user.getRoles();

                // 권한 없음
                if (!isAuthorized(requestPath, roles)) {
                    return unauthorizedResponse(exchange, ErrorMessage.FORBIDDEN);
                }

                // 헤더 추가
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header(HEADER_NAME, username)
                        .build();
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();
                return chain.filter(mutatedExchange);
            } else {
                return unauthorizedResponse(exchange, ErrorMessage.INVALID_TOKEN);
            }
        } catch (Exception e) {
            return responseWriter.setErrorResponse(exchange, ErrorMessage.INTERNAL_SERVER_ERROR.getStatus(),
                    ErrorMessage.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HEADER_STRING.getValue());
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX.getValue())) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isAuthorized(String requestPath, List<Role> roles) {
        if (requestPath.startsWith(ApiPrefix.OWNER.getValue())) {
            return roles.contains(Role.OWNER) || roles.contains(Role.MANAGER) || roles.contains(Role.MASTER);
        }
        if (requestPath.startsWith(ApiPrefix.ADMIN.getValue())) {
            return roles.contains(Role.MANAGER) || roles.contains(Role.MASTER);
        }
        if (requestPath.startsWith(ApiPrefix.MASTER.getValue())) {
            return roles.contains(Role.MASTER);
        }
        return true;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, ErrorMessage errorMessage) {
        exchange.getResponse().setStatusCode(errorMessage.getStatus());
        return responseWriter.setErrorResponse(exchange, errorMessage.getStatus(), errorMessage.getMessage());
    }

}
