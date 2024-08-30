package com.ThreeK_Project.api_server.domain.order.repository;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "select o from Order o " +
            "left join fetch o.payment " +
            "left join fetch o.orderProducts op " +
            "left join fetch op.product " +
            "where o.orderId = :orderId")
    public Optional<Order> findByIdWithProductsAndPayment(@Param("orderId") UUID orderId);

}
