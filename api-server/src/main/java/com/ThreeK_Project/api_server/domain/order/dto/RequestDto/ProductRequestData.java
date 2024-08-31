package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestData {
    private UUID productId;
    private Integer quantity;
    private BigDecimal productAmount;
}
