package com.ThreeK_Project.api_server.domain.user.entity;

import com.ThreeK_Project.api_server.domain.user.enums.Role;
import com.ThreeK_Project.api_server.global.audit.UserAuditEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_users")
public class User extends UserAuditEntity {

    @Id
    @NotNull
    @Column(unique = true, name = "username")
    private String username;

    @NotNull
    @Column(name = "password", length = 60)
    private String password;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String address;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "p_user_roles", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public static User createUser(String username, String encodedPassword, Role role, String phoneNumber,
                                  String address) {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        User user =  User.builder()
                .username(username)
                .password(encodedPassword)
                .roles(roles)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
        user.initUserAuditData(user);
        return user;
    }

    public static User updateUser(String username, String encodedPassword, List<Role> originalRoles, String phoneNumber, String address, LocalDateTime originalCreatedAt) {
        User user =  User.builder()
                .username(username)
                .password(encodedPassword)
                .phoneNumber(phoneNumber)
                .address(address)
                .roles(originalRoles)
                .build();
        user.initUserAuditData(user);
        user.setOriginalCreatedAt(originalCreatedAt);
        return user;
    }

    public void deleteUser(User user) {
        this.deleteUserAuditData(user);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addRole(Role role, User master) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
            this.updateUserAuditData(master);
        }
    }

    public void removeRole(Role role, User master) {
        if (this.roles.contains(role)) {
            this.roles.remove(role);
            this.updateUserAuditData(master);
        }
    }

}