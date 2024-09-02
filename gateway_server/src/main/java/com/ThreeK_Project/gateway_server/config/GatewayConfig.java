package com.ThreeK_Project.gateway_server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // public api
                .route("api-server", r -> r.path("/api/public/**")
                        .uri("lb://api-server"))
                // auth api
                .route("api-server", r -> r.path("/api/**")
                        .filters(f -> f.filter((exchange, chain) -> jwtAuthorizationFilter.filter(exchange, chain)))
                        .uri("lb://api-server"))
                .build();
    }

}

