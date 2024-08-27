package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.CategoryRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.LocationRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateRestaurantTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateRestaurant_Success() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User owner = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);
        Location location = mock(Location.class);
        Category category = mock(Category.class);
        RestaurantRequest request = new RestaurantRequest(
                "Updated Restaurant Name",
                "Updated Address",
                "Updated Phone Number",
                "Updated Description",
                1,
                1
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getUser()).thenReturn(owner);
        when(owner.getUsername()).thenReturn("ownerUsername");
        when(locationRepository.findById(request.getLocationId())).thenReturn(Optional.of(location));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

        // When
        String result = restaurantService.updateRestaurant(request, restaurantId, owner);

        // Then
        assertEquals("가게 수정 성공", result);
        verify(restaurant).updateRestaurant(
                request.getName(),
                request.getAddress(),
                request.getPhoneNumber(),
                request.getDescription(),
                location,
                category
        );
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void updateRestaurant_NoPermission() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User owner = mock(User.class);
        User differentUser = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);
        RestaurantRequest request = new RestaurantRequest(
                "Updated Restaurant Name",
                "Updated Address",
                "Updated Phone Number",
                "Updated Description",
                1,
                1
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getUser()).thenReturn(owner);
        when(owner.getUsername()).thenReturn("ownerUsername");
        when(differentUser.getUsername()).thenReturn("differentUsername");

        // When & Then
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            restaurantService.updateRestaurant(request, restaurantId, differentUser);
        });

        assertEquals("가게를 수정할 권한이 없습니다.", exception.getMessage());
        verify(restaurant, never()).updateRestaurant(anyString(), anyString(), anyString(), anyString(), any(Location.class), any(Category.class));
        verify(restaurantRepository, never()).save(restaurant);
    }

    @Test
    void updateRestaurant_LocationNotFound() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User owner = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);
        RestaurantRequest request = new RestaurantRequest(
                "Updated Restaurant Name",
                "Updated Address",
                "Updated Phone Number",
                "Updated Description",
                1,
                1
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getUser()).thenReturn(owner);
        when(owner.getUsername()).thenReturn("ownerUsername");
        when(locationRepository.findById(request.getLocationId())).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            restaurantService.updateRestaurant(request, restaurantId, owner);
        });

        assertEquals("위치 조회 실패", exception.getMessage());
        verify(restaurant, never()).updateRestaurant(anyString(), anyString(), anyString(), anyString(), any(Location.class), any(Category.class));
        verify(restaurantRepository, never()).save(restaurant);
    }

    @Test
    void updateRestaurant_CategoryNotFound() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User owner = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);
        Location location = mock(Location.class);
        RestaurantRequest request = new RestaurantRequest(
                "Updated Restaurant Name",
                "Updated Address",
                "Updated Phone Number",
                "Updated Description",
                1,
                1
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getUser()).thenReturn(owner);
        when(owner.getUsername()).thenReturn("ownerUsername");
        when(locationRepository.findById(request.getLocationId())).thenReturn(Optional.of(location));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            restaurantService.updateRestaurant(request, restaurantId, owner);
        });

        assertEquals("카테고리 조회 실패", exception.getMessage());
        verify(restaurant, never()).updateRestaurant(anyString(), anyString(), anyString(), anyString(), any(Location.class), any(Category.class));
        verify(restaurantRepository, never()).save(restaurant);
    }
}
