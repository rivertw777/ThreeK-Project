package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
