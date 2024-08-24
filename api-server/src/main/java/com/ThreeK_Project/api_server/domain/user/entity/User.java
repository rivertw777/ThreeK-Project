package com.ThreeK_Project.api_server.domain.user.entity;

import com.ThreeK_Project.api_server.global.audit.BaseEntity;
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

    @Column(name = "password", length = 60)
    private String password;

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "p_user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public static User createUser(String username, String encodedPassword, Role role) {
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .roles(Collections.singletonList(role))
                .build();
        return user;
    }

}