package com.ThreeK_Project.api_server.domain.product.entity;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;
    private String name;
    private Integer price;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public static Product createProduct(
            String name, Integer price, String description, Restaurant restaurant
    ) {
        return Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .restaurant(restaurant)
                .build();
    }
}
