package com.ThreeK_Project.api_server.domain.order.entity;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_order_products")
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    private Integer quantity;
    private Integer totalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public static OrderProduct createOrderProduct(
            Integer quantity, Integer totalPrice, Product product, Order order
    ) {
        return OrderProduct.builder()
                .quantity(quantity)
                .totalPrice(totalPrice)
                .product(product)
                .order(order)
                .build();
    }
}
