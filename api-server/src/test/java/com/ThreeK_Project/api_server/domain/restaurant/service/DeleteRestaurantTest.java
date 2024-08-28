package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.user.entity.User;
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
import static org.mockito.Mockito.*;

class DeleteRestaurantTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteRestaurant_Success() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User owner = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getUser()).thenReturn(owner);
        when(owner.getUsername()).thenReturn("ownerUsername");

        // When
        String result = restaurantService.deleteRestaurant(restaurantId, owner);

        // Then
        assertEquals("가게 삭제 성공", result);
        verify(restaurantRepository, times(1)).save(restaurant); // Verifies that save was called
        verify(restaurantRepository, never()).deleteById(any(UUID.class)); // Verifies that deleteById was not called
    }




    @Test
    void deleteRestaurant_NoPermission() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User owner = mock(User.class);
        User differentUser = mock(User.class);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getUser()).thenReturn(owner);
        when(owner.getUsername()).thenReturn("ownerUsername");
        when(differentUser.getUsername()).thenReturn("differentUsername");

        // When & Then
        SecurityException exception = assertThrows(SecurityException.class, () -> {
            restaurantService.deleteRestaurant(restaurantId, differentUser);
        });

        assertEquals("가게를 삭제할 권한이 없습니다.", exception.getMessage());
        verify(restaurantRepository, never()).deleteById(restaurantId);
    }

    @Test
    void deleteRestaurant_RestaurantNotFound() {
        // Given
        UUID restaurantId = UUID.randomUUID();
        User user = mock(User.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            restaurantService.deleteRestaurant(restaurantId, user);
        });

        assertEquals("가게 조회 실패", exception.getMessage());
        verify(restaurantRepository, never()).deleteById(restaurantId);
    }
}
