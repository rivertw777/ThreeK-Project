package com.ThreeK_Project.api_server.domain.order.repository;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
