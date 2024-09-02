package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantSearch;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> searchRestaurants(RestaurantSearch restaurantSearch, Pageable pageable);
}
