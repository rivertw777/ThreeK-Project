package com.ThreeK_Project.api_server.global.security.config;

import static com.ThreeK_Project.api_server.global.security.message.SecurityExceptionMessage.UNAUTHORIZED;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.ThreeK_Project.api_server.global.security.utils.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// 인증 실패 (401)
@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointCustom implements AuthenticationEntryPoint {

    private final ResponseWriter responseWriter;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {
        responseWriter.setErrorResponse(response, SC_UNAUTHORIZED, UNAUTHORIZED.getValue());
    }

}