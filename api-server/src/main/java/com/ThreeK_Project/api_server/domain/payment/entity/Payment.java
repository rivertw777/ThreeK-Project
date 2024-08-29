package com.ThreeK_Project.api_server.domain.payment.entity;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.payment.dto.UpdatePaymentDto;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import com.ThreeK_Project.api_server.domain.payment.enums.converter.PaymentMethodConverter;
import com.ThreeK_Project.api_server.domain.payment.enums.converter.PaymentStatusConverter;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentMethod;
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus;
    private BigDecimal paymentAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public static Payment createPayment(
            PaymentMethod paymentMethod, PaymentStatus paymentStatus, BigDecimal paymentAmount, Order order
    ) {
        return Payment.builder()
                .paymentMethod(paymentMethod)
                .paymentStatus(paymentStatus)
                .paymentAmount(paymentAmount)
                .order(order)
                .build();
    }

    public void updatePayment(UpdatePaymentDto requestDto) {
        this.paymentMethod = requestDto.getPaymentMethod();
        this.paymentStatus = requestDto.getPaymentStatus();
        this.paymentAmount = requestDto.getAmount();
    }
}
