package com.ThreeK_Project.api_server.domain.order.dto.ResponseDto;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private UUID orderId;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private BigDecimal orderAmount;
    private String deliveryAddress;
    private String deliveryDetails;
    private PaymentResponseData orderPayment;
    private List<ProductResponseData> orderedProducts;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.orderStatus = order.getOrderStatus();
        this.orderType = order.getOrderType();
        this.orderAmount = order.getOrderAmount();
        this.deliveryAddress = order.getDeliveryAddress();
        this.deliveryDetails = order.getDeliveryDetails();
        this.orderPayment = order.getPayment() == null ? null : new PaymentResponseData(order.getPayment());
        this.orderedProducts = order.getOrderProducts().stream()
                .map(ProductResponseData::new)
                .toList();
    }
}
