package com.ThreeK_Project.api_server.domain.user.dto.response;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ManagerUserInfoResponse(String username, List<String> roles, String phoneNumber, String address,
                                      LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt,
                                      String updatedBy, LocalDateTime deletedAt, String deletedBy) {
    public ManagerUserInfoResponse(User user) {
        this(user.getUsername(),
                user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getCreatedBy().getUsername(),
                user.getUpdatedAt(),
                user.getUpdatedBy().getUsername(),
                user.getDeletedAt() != null ? user.getDeletedAt() : null,
                user.getDeletedBy() != null ? user.getDeletedBy().getUsername() : null);
    }
}
