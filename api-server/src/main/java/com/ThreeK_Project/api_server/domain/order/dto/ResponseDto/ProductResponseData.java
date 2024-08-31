package com.ThreeK_Project.api_server.domain.order.dto.ResponseDto;

import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ProductResponseData {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal productAmount;

    public ProductResponseData(OrderProduct orderProduct) {
        this.productId = orderProduct.getProduct().getProductId();
        this.productName = orderProduct.getProduct().getName();
        this.quantity = orderProduct.getQuantity();
        this.productAmount = orderProduct.getTotalPrice();
    }
}
