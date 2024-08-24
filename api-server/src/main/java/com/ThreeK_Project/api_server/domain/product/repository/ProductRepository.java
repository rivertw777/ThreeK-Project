package com.ThreeK_Project.api_server.domain.product.repository;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
