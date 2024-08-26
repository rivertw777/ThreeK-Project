package com.ThreeK_Project.api_server.domain.restaurant.dto;


import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {

    // 가게 id
    private UUID restaurantId;
    // 가게 이름
    private String name;
    // 가게 주소
    private String address;
    // 가게 전화번호
    private String phoneNumber;
    // 가게 설명
    private String description;
    // 가게 위치 id
    private int locationId;
    // 가게 카테고리 id
    private Long categoryId;

    public RestaurantResponse(Restaurant restaurant) {
        this.restaurantId = restaurant.getRestaurantId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.description = restaurant.getDescription();
        this.locationId = (restaurant.getLocation() != null) ? restaurant.getLocation().getLocationId() : null;
        this.categoryId = (restaurant.getCategory() != null) ? restaurant.getCategory().getCategoryId() : null;
    }
}
