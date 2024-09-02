package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusRequestDto {
    @NotNull(message = "OrderStatus is null")
    private OrderStatus orderStatus;
}