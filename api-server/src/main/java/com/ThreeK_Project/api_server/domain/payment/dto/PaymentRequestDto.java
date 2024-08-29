package com.ThreeK_Project.api_server.domain.payment.dto;

import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private PaymentMethod paymentMethod;
    private BigDecimal paymentAmount;
}
