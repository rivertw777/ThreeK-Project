package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequestDto {
    private OrderStatus orderStatus;
}