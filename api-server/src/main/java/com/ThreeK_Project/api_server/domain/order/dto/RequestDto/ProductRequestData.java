package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Product Quantity is null")
    @Min(value = 1, message = "Product Quantity is less than 1")
    private Integer quantity;
    @NotNull(message = "ProductAmount is null")
    @Min(value = 1, message = "ProductAmount is less than 1")
    private BigDecimal productAmount;
}
