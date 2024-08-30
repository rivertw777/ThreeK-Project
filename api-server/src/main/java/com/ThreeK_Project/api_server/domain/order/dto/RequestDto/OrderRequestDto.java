package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private BigDecimal orderAmount;
    private String deliveryAddress;
    private String requestDetails;
    private OrderType orderType;
    private UUID restaurantId;
    private List<ProductRequestData> productList;
}
