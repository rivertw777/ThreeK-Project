package com.ThreeK_Project.api_server.domain.user.dto.response;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record ManagerUserInfoResponse(String username, List<String> roles, String phoneNumber, String address,
                                      String createdAt, String createdBy, String updatedAt,
                                      String updatedBy, String deletedAt, String deletedBy) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ManagerUserInfoResponse(User user) {
        this(user.getUsername(),
                user.getRoles().stream().map(Role::getValue).collect(Collectors.toList()),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getCreatedAt().format(formatter),
                user.getCreatedBy().getUsername(),
                user.getUpdatedAt().format(formatter),
                user.getUpdatedBy().getUsername(),
                user.getDeletedAt() != null ? user.getDeletedAt().format(formatter) : null,
                user.getDeletedBy() != null ? user.getDeletedBy().getUsername() : null);
    }
}
