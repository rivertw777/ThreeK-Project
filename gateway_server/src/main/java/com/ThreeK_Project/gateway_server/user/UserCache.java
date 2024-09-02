package com.ThreeK_Project.gateway_server.user;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class UserCache {

    private String username;

    private String password;

    private String phoneNumber;

    private String address;

    private List<Role> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

}