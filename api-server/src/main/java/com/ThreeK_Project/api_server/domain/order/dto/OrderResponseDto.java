package com.ThreeK_Project.api_server.domain.order.dto;

import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.entity.OrderProduct;
import com.ThreeK_Project.api_server.domain.order.enums.OrderStatus;
import com.ThreeK_Project.api_server.domain.order.enums.OrderType;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private UUID orderId;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private BigDecimal orderAmount;
    private String deliveryAddress;
    private String deliveryDetails;
    private List<ProductResponseData> orderedProducts;

    public OrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.orderStatus = order.getOrderStatus();
        this.orderType = order.getOrderType();
        this.orderAmount = order.getOrderAmount();
        this.deliveryAddress = order.getDeliveryAddress();
        this.deliveryDetails = order.getDeliveryDetails();

        this.orderedProducts = new ArrayList<>();
        for(OrderProduct orderProduct: order.getOrderProducts()){
            Product product = orderProduct.getProduct();
            orderedProducts.add(new ProductResponseData(
                    product.getProductId(), product.getName(), orderProduct.getQuantity(), orderProduct.getTotalPrice()));
        }
    }
}
