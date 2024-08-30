package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> searchRestaurants(String name, String address, String phoneNumber, String description, String username, Integer locationId, Integer categoryId, Pageable pageable);
}
