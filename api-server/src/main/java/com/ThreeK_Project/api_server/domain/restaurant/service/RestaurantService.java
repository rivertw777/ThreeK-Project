package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.CategoryRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.LocationRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    public String registRestaurant(RestaurantRequest restaurantRequest) {

        Location location = locationRepository.findById(restaurantRequest.getLocationId())
                .orElseThrow(() -> new NotFoundException("위치 조회 실패"));
        Category category = categoryRepository.findById(restaurantRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("카테고리 조회 실패"));


        Restaurant restaurant = Restaurant.createRestaurant(
                restaurantRequest.getName(),
                restaurantRequest.getAddress(),
                restaurantRequest.getPhoneNumber(),
                restaurantRequest.getDescription(),
                location,
                category
        );

        restaurantRepository.save(restaurant);

        return "가게 등록 성공";
    }
}
