package com.ThreeK_Project.api_server.global.security.config;

import static com.ThreeK_Project.api_server.global.security.message.SecurityExceptionMessage.NO_AUTHORITY;
import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import com.ThreeK_Project.api_server.global.security.utils.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

// 권한이 없음 (403)
@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerCustom implements AccessDeniedHandler {

    private final ResponseWriter responseWriter;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        responseWriter.setErrorResponse(response, SC_FORBIDDEN, NO_AUTHORITY.getValue());
    }

}