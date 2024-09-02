package com.ThreeK_Project.api_server.domain.product.dto;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID productId;
    private String name;
    private Integer price;
    private String description;
    private UUID restaurantId;

    public ProductResponse(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.restaurantId = product.getRestaurant().getRestaurantId();
    }
}
