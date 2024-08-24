package com.ThreeK_Project.api_server.domain.payment.entity;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_payments")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;
    private String paymentMethod;
    private String paymentStatus;
    private Integer paymentAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static Payment createPayment(
            String paymentMethod, String paymentStatus, Integer paymentAmount, Order order
    ) {
        return Payment.builder()
                .paymentMethod(paymentMethod)
                .paymentStatus(paymentStatus)
                .paymentAmount(paymentAmount)
                .order(order)
                .build();
    }
}
