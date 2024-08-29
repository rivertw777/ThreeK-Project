package com.ThreeK_Project.api_server.domain.user.controller;

import com.ThreeK_Project.api_server.domain.user.dto.AssignRoleRequest;
import com.ThreeK_Project.api_server.domain.user.dto.RevokeRoleRequest;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/api/users")
    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody SignUpRequest reqeustParam) {
        SuccessResponse response = userService.signUp(reqeustParam);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/api/users")
    public ResponseEntity<UserInfoResponse> getUserInfo(){
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        UserInfoResponse response = userService.getUserInfo(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 정보 수정")
    @PutMapping("/api/users")
    public ResponseEntity<SuccessResponse> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest requestParam){
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = userService.updateUserInfo(user, requestParam);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/api/users")
    public ResponseEntity<SuccessResponse> deleteUser(){
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = userService.deleteUser(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "MASTER 권한 부여")
    @PatchMapping("api/master/users/{username}/roles")
    public ResponseEntity<SuccessResponse> assignRoleToUser(@Valid @RequestBody AssignRoleRequest requestParam,
                                                            @Valid @PathVariable("username") String username) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = userService.assignRoleToUser(user, username, requestParam);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "MASTER 권한 회수")
    @DeleteMapping("api/master/users/{username}/roles")
    public ResponseEntity<SuccessResponse> revokeRoleFromUser(@Valid @RequestBody RevokeRoleRequest requestParam,
                                                    @Valid @PathVariable("username") String username) {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        SuccessResponse response = userService.revokeRoleFromUser(user, username, requestParam);
        return ResponseEntity.ok(response);
    }

}
