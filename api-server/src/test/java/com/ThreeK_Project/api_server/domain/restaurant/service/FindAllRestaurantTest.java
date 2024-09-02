package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantSearch;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FindAllRestaurantTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllRestaurant_Success() {

        // Given: Restaurant 객체들을 생성
        Restaurant restaurant1 = Restaurant.createRestaurant(
                "Test Restaurant 1", "Test Address 1", "1234567890", "Test Description 1", null, null, null
        );

        Restaurant restaurant2 = Restaurant.createRestaurant(
                "Test Restaurant 2", "Test Address 2", "0987654321", "Test Description 2", null, null, null
        );

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);
        Pageable pageable = PageRequest.of(0, 10);

        RestaurantSearch requestSearch = new RestaurantSearch();  // 검색 조건을 설정할 수 있습니다.

        // When: restaurantRepository.searchRestaurants가 호출되면 restaurantPage를 반환하도록 설정
        when(restaurantRepository.searchRestaurants(any(RestaurantSearch.class), any(Pageable.class)))
                .thenReturn(restaurantPage);

        // Then: searchRestaurants 메서드 호출 및 검증
        Page<RestaurantResponse> result = restaurantService.searchRestaurants(requestSearch, pageable);

        // 결과 리스트의 크기와 각 요소의 필드를 확인
        assertEquals(2, result.getTotalElements());
        assertEquals("Test Restaurant 1", result.getContent().get(0).getName());
        assertEquals("Test Restaurant 2", result.getContent().get(1).getName());
    }
}
