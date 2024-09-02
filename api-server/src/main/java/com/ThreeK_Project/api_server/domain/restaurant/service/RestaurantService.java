package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantSearch;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.CategoryRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.LocationRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.domain.user.enums.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public String registRestaurant(RestaurantRequest restaurantRequest, User user) {

        Location location = findLocationById(restaurantRequest.getLocationId());
        Category category = findCategoryById(restaurantRequest.getCategoryId());

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

    public RestaurantResponse findRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(restaurant -> new RestaurantResponse(restaurant))
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));
    }

    @Transactional
    public String updateRestaurant(RestaurantRequest restaurantRequest, UUID restaurantId, User user) {
        // 권한 확인
        Restaurant restaurant = validateAndGetRestaurant(restaurantId, user);

        Location location = findLocationById(restaurantRequest.getLocationId());
        Category category = findCategoryById(restaurantRequest.getCategoryId());

        restaurant.updateRestaurant(
                restaurantRequest.getName(),
                restaurantRequest.getAddress(),
                restaurantRequest.getPhoneNumber(),
                restaurantRequest.getDescription(),
                location,
                category
        );

        restaurantRepository.save(restaurant);

        return "가게 수정 성공";
    }

    @Transactional
    public String deleteRestaurant(UUID restaurantId, User user) {
        // 권한 확인
        Restaurant restaurant = validateAndGetRestaurant(restaurantId, user);

        // 물리적 삭제 대신 논리적 삭제 처리
        restaurant.deleteBy(user);

        return "가게 삭제 성공";
    }

    public Page<RestaurantResponse> searchRestaurants(RestaurantSearch restaurantSearch, Pageable pageable) {
        return restaurantRepository.searchRestaurants(restaurantSearch, pageable)
                .map(RestaurantResponse::new); // map을 사용하여 Page<Restaurant>를 Page<RestaurantResponse>로 변환
    }

    private Location findLocationById(Integer locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("위치 조회 실패"));
    }

    private Category findCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("카테고리 조회 실패"));
    }

    public Restaurant validateAndGetRestaurant(UUID restaurantId, User user) {
        // 레스토랑 존재 여부 확인
        Restaurant restaurant = getRestaurant(restaurantId);

        // 권한 확인: 가게 주인인지 검증
        if (!validUser(restaurant, user)) {
            throw new SecurityException("가게에 대한 권한이 없습니다.");
        }

        // 검증된 레스토랑 엔티티 반환
        return restaurant;
    }

    public Restaurant getRestaurant(UUID restaurantId) {
        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));
        // 검증된 레스토랑 엔티티 반환
        return restaurant;
    }

    public boolean validUser(Restaurant restaurant, User user) {
        // 권한 확인: 가게 주인인지 검증
        return restaurant.getUser().getUsername().equals(user.getUsername())
                || user.getRoles().contains(Role.MASTER)
                || user.getRoles().contains(Role.MANAGER);
    }
}
