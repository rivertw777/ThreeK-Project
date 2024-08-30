package com.ThreeK_Project.api_server.domain.product.repository;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchProductsByKeyword(String keyword, Pageable pageable);
}
