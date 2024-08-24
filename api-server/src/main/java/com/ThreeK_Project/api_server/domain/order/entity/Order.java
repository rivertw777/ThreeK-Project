package com.ThreeK_Project.api_server.domain.order.entity;

import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;
    private String orderType;
    private String orderStatus;
    private Integer orderAmount;
    private String deliveryAddress;
    private String deliveryDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurants_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts;

    @OneToOne(fetch = FetchType.LAZY)
    private Payment payment;

    public static Order createOrder(
            String orderType, String orderStatus, Integer orderAmount, String deliveryAddress,
            String deliveryDetails, Restaurant restaurant
    ) {
        return Order.builder()
                .orderType(orderType)
                .orderStatus(orderStatus)
                .orderAmount(orderAmount)
                .deliveryAddress(deliveryAddress)
                .deliveryDetails(deliveryDetails)
                .restaurant(restaurant)
                .build();
    }
}
