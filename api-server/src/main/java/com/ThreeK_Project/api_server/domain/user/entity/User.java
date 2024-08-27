package com.ThreeK_Project.api_server.domain.user.entity;

import com.ThreeK_Project.api_server.domain.user.enums.Role;
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
import java.util.ArrayList;
import java.util.Collections;
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
public class User {

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
    @CollectionTable(name = "p_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public static User createUser(String username, String encodedPassword, Role role, String phoneNumber,
                                  String address) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .roles(Collections.singletonList(role))
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
    }

    public void updateUserInfo(String username, String encodedPassword, String phoneNumber, String address) {
        setUsername(username);
        setPassword(encodedPassword);
        setPhoneNumber(phoneNumber);
        setAddress(address);
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

}