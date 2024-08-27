package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.CategoryRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.LocationRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    public String registRestaurant(RestaurantRequest restaurantRequest, User user) {

        Location location = locationRepository.findById(restaurantRequest.getLocationId())
                .orElseThrow(() -> new NotFoundException("위치 조회 실패"));
        Category category = categoryRepository.findById(restaurantRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("카테고리 조회 실패"));

        Restaurant restaurant = Restaurant.createRestaurant(
                restaurantRequest.getName(),
                restaurantRequest.getAddress(),
                restaurantRequest.getPhoneNumber(),
                restaurantRequest.getDescription(),
                user,
                location,
                category
        );

        restaurantRepository.save(restaurant);

        return "가게 등록 성공";
    }

    public List<RestaurantResponse> findAllRestaurant() {
        List<RestaurantResponse> responseList = new ArrayList<>();
        restaurantRepository.findAll().forEach(restaurant -> {
            responseList.add(new RestaurantResponse(restaurant));
        });
        return responseList;
    }

    public RestaurantResponse findRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> new RestaurantResponse(restaurant))
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));
    }
}
