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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public String updateRestaurant(RestaurantRequest restaurantRequest, UUID restaurantId, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));

        if (!restaurant.getUser().getUsername().equals(user.getUsername())) {
            throw new SecurityException("가게를 수정할 권한이 없습니다.");
        }

        Location location = locationRepository.findById(restaurantRequest.getLocationId())
                .orElseThrow(() -> new NotFoundException("위치 조회 실패"));
        Category category = categoryRepository.findById(restaurantRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("카테고리 조회 실패"));

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

    public String deleteRestaurant(UUID restaurantId, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));

        if (!restaurant.getUser().getUsername().equals(user.getUsername())) {
            throw new SecurityException("가게를 삭제할 권한이 없습니다.");
        }
        // 물리적 삭제 대신 논리적 삭제 처리
        restaurant.deleteBy(user);
        restaurantRepository.save(restaurant);  // 상태 변경을 DB에 반영

        return "가게 삭제 성공";
    }

    public Restaurant validateAndGetRestaurant(UUID restaurantId, User user) {
        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));

        // 권한 확인: 가게 주인인지 검증
        if (!restaurant.getUser().getUsername().equals(user.getUsername())
                || user.getRoles().equals("MANAGER")
                || user.getRoles().equals("MASTER")) {
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

    public Page<RestaurantResponse> searchRestaurants(String name, String address, String phoneNumber, String description, String username, Integer locationId, Integer categoryId, Pageable pageable) {
        return restaurantRepository.searchRestaurants(name, address, phoneNumber, description, username, locationId, categoryId, pageable)
                .map(RestaurantResponse::new); // map을 사용하여 Page<Restaurant>를 Page<RestaurantResponse>로 변환
    }

    public String getRestaurantName(UUID restaurantId) {
        // 레스토랑 존재 여부 확인
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("가게 조회 실패"));
        // 검증된 레스토랑 엔티티 반환
        return restaurant.getName();
    }
}
