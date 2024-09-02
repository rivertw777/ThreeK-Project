package com.ThreeK_Project.api_server.domain.auth.controller;

import com.ThreeK_Project.api_server.domain.auth.dto.LoginRequest;
import com.ThreeK_Project.api_server.domain.auth.dto.LoginResponse;
import com.ThreeK_Project.api_server.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원 로그인")
    @PostMapping("/api/public/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest requestParam) {
        LoginResponse response = authService.login(requestParam);
        return ResponseEntity.ok(response);
    }

}
