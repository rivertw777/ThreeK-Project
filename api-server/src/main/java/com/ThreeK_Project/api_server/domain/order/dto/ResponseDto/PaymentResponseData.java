package com.ThreeK_Project.api_server.domain.order.dto.ResponseDto;

import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentResponseData {
    private UUID paymentId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private BigDecimal paymentAmount;

    public PaymentResponseData(Payment payment){
        this.paymentId = payment.getPaymentId();
        this.paymentStatus = payment.getPaymentStatus();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentAmount = payment.getPaymentAmount();
    }
}
