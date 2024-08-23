package com.ThreeK_Project.api_server.domain.user.repository;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
