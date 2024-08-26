package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FindByIdRestaurantTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private Location location;

    @Mock
    private Category category;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdRestaurant_Success() {
        // Given
        UUID restaurantId = UUID.randomUUID();

        // Mock the getLocationId and getCategoryId methods to return specific IDs
        when(location.getLocationId()).thenReturn(1);
        when(category.getCategoryId()).thenReturn(1L);

        Restaurant restaurant = Restaurant.createRestaurant(
                "Test Restaurant", "123 Test St.", "123-456-7890", "Great food", location, category
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // When
        RestaurantResponse result = restaurantService.findRestaurantById(restaurantId);

        // Then
        assertEquals(restaurant.getName(), result.getName());
        assertEquals(restaurant.getAddress(), result.getAddress());
        assertEquals(restaurant.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(restaurant.getDescription(), result.getDescription());
        assertEquals(1, result.getLocationId());  // 모킹된 locationId 확인
        assertEquals(1L, result.getCategoryId());  // 모킹된 categoryId 확인
    }

    @Test
    void findRestaurantById_NotFound() {
        // Given
        UUID restaurantId = UUID.randomUUID();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            restaurantService.findRestaurantById(restaurantId);
        });
    }
}
