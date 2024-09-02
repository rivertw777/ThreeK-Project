package com.ThreeK_Project.api_server.domain.order.repository;

import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
