package com.ThreeK_Project.api_server.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseData {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal amount;
}
