package com.ThreeK_Project.api_server.domain.product.entity;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
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

    public void updateProduct(String name, Integer price, String description) {
        if (name != null && !name.isEmpty()) {this.name = name;}
        if (price != null) {this.price = price;}
        if (description != null && !description.isEmpty()) {this.description = description;}
    }

}
