package com.ThreeK_Project.api_server.domain.user.dto;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record UserInfoResponse(String username, List<String> roles, String phoneNumber, String address,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
    public UserInfoResponse(User user) {
        this(user.getUsername(),
                user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
