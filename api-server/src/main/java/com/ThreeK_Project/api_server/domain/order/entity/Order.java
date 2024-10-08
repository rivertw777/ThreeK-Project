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
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_orders")
@SQLRestriction("deleted_at is NULL")
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
    @Builder.Default
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy ="order", fetch = FetchType.LAZY)
    private Payment payment;

    public static Order createOrder(
            OrderType orderType, OrderStatus orderStatus, BigDecimal orderAmount, String deliveryAddress,
            String deliveryDetails, Restaurant restaurant
    ) {
        Order order =  Order.builder()
                .orderType(orderType)
                .orderStatus(orderStatus)
                .orderAmount(orderAmount)
                .deliveryAddress(deliveryAddress)
                .deliveryDetails(deliveryDetails)
                .restaurant(restaurant)
                .orderProducts(new ArrayList<>())
                .build();

        // restaurant에 order 넣어야됨

        return order;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
