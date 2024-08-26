package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                "Test Restaurant 1", "Test Address 1", "1234567890", "Test Description 1", null, null
        );

        Restaurant restaurant2 = Restaurant.createRestaurant(
                "Test Restaurant 2", "Test Address 2", "0987654321", "Test Description 2", null, null
        );

        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);

        // When: restaurantRepository.findAll()이 호출되면 restaurants 리스트를 반환하도록 설정
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        // Then: findAllRestaurant() 메서드 호출 및 검증
        List<RestaurantResponse> result = restaurantService.findAllRestaurant();

        // 결과 리스트의 크기와 각 요소의 필드를 확인
        assertEquals(2, result.size());
        assertEquals("Test Restaurant 1", result.get(0).getName());
        assertEquals(0, result.get(0).getLocationId());
        assertEquals(0, result.get(0).getCategoryId());
        assertEquals("Test Restaurant 2", result.get(1).getName());
        assertEquals(0, result.get(1).getLocationId());
        assertEquals(0, result.get(1).getCategoryId());
    }
}
