package com.ThreeK_Project.gateway_server.utils;

import com.ThreeK_Project.gateway_server.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ResponseWriter {

    private final ObjectMapper objectMapper;

    public Mono<Void> setErrorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ErrorResponse errorResponse = new ErrorResponse(message);
        try {
            byte[] responseBody = objectMapper.writeValueAsBytes(errorResponse);
            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            return exchange.getResponse().writeWith(Mono.just(bufferFactory.wrap(responseBody)));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }

}
