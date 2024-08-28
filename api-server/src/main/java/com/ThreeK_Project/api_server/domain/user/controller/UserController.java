package com.ThreeK_Project.api_server.domain.user.controller;

import com.ThreeK_Project.api_server.domain.user.dto.SignUpRequest;
import com.ThreeK_Project.api_server.domain.user.dto.UpdateUserInfoRequest;
import com.ThreeK_Project.api_server.domain.user.dto.UserInfoResponse;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.service.UserService;
import com.ThreeK_Project.api_server.global.dto.SuccessResponse;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping
    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody SignUpRequest reqeustParam) {
        SuccessResponse response = userService.signUp(reqeustParam);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(){
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        UserInfoResponse response = userService.getUserInfo(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping
    public ResponseEntity<SuccessResponse> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest requestParam){
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = userService.updateUserInfo(user, requestParam);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteUser(){
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = userService.deleteUser(user);
        return ResponseEntity.ok(response);
    }

}
