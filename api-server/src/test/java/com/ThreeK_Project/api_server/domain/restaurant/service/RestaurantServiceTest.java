package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchRestaurants() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Restaurant restaurant = Restaurant.createRestaurant(
                "김치찌개 맛집", "서울", "010-1234-5678", "맛있는 김치찌개",
                null, null, null
        );
        List<Restaurant> restaurants = Collections.singletonList(restaurant);
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants);

        when(restaurantRepository.searchRestaurants(any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(restaurantPage);

        // When
        Page<RestaurantResponse> result = restaurantService.searchRestaurants("김치", null, null, null, null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getName()).isEqualTo("김치찌개 맛집");
    }
}
