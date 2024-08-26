package com.ThreeK_Project.api_server.domain.order.entity;

import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import com.ThreeK_Project.api_server.domain.order.enums.converter.OrderStatusConverter;
import com.ThreeK_Project.api_server.domain.order.enums.converter.OrderTypeConverter;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @Convert(converter = OrderTypeConverter.class)
    private OrderType orderType;
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus orderStatus;
    private BigDecimal orderAmount;
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
            OrderType orderType, OrderStatus orderStatus, BigDecimal orderAmount, String deliveryAddress,
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
