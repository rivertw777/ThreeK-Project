package com.ThreeK_Project.api_server.domain.payment.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotNull(message = "Payment Method is null")
    private PaymentMethod paymentMethod;
    @NotNull(message = "Payment Amount is null")
    @Min(value = 1, message = "Payment Amount is less than 1")
    private BigDecimal paymentAmount;
}
