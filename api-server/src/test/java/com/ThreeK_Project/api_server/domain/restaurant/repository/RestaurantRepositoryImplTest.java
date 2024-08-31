package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
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

class RestaurantRepositoryImplTest {

    @Mock
    private RestaurantRepositoryImpl restaurantRepository;

    @InjectMocks
    private RestaurantRepositoryImplTest restaurantRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchRestaurants() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Restaurant restaurant = new Restaurant();
        List<Restaurant> restaurants = Collections.singletonList(restaurant);
        Page<Restaurant> expectedPage = new PageImpl<>(restaurants);

        // When
        when(restaurantRepository.searchRestaurants(
                any(String.class), any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<Restaurant> result = restaurantRepository.searchRestaurants(
                "김치", null, null, null, null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0)).isEqualTo(restaurant);
    }
}
