package com.ThreeK_Project.api_server.domain.order.dto.RequestDto;

import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "OrderAmount is empty")
    @Min(value = 1, message = "OrderAmount is less then 1")
    private BigDecimal orderAmount;

    @NotBlank(message = "deliveryAddress is empty")
    private String deliveryAddress;

    private String requestDetails;

    @NotNull(message = "orderType is empty")
    private OrderType orderType;

    @Valid
    @NotNull(message = "ProductList is empty")
    @Size(min = 1, message = " ProductList is empty")
    private List<ProductRequestData> productList;
}
