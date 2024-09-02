package com.ThreeK_Project.api_server.domain.payment.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.payment.enums.PaymentMethod;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDto {
    @NotNull(message = "Payment Method is null")
    private PaymentMethod paymentMethod;
    @NotNull(message = "Payment Status is not null")
    private PaymentStatus paymentStatus;
    @NotNull(message = "Payment Amount is null")
    @Min(value = 1, message = "Payment Amount is less than 1")
    private BigDecimal amount;
}
