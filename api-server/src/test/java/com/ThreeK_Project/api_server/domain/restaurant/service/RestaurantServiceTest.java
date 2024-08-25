package com.ThreeK_Project.api_server.domain.restaurant.service;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Category;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Location;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.CategoryRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.LocationRepository;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

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
    void registRestaurant_Success() {
        // given
        RestaurantRequest restaurantRequest = new RestaurantRequest(
                "Test Restaurant",
                "123 Test Address",
                "123-456-7890",
                "Test Description",
                2,  // Assume locationId
                5L  // Assume categoryId
        );

        Location location = Location.createLocation("종로구");
        Category category = Category.createCategory("한식");

        when(locationRepository.findById(restaurantRequest.getLocationId())).thenReturn(Optional.of(location));
        when(categoryRepository.findById(restaurantRequest.getCategoryId())).thenReturn(Optional.of(category));

        // when
        String result = restaurantService.registRestaurant(restaurantRequest);

        // then
        assertEquals("가게 등록 성공", result);
        System.out.println("가게 등록 성공");
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void registRestaurant_LocationNotFound() {
        // given
        RestaurantRequest restaurantRequest = new RestaurantRequest(
                "Test Restaurant",
                "123 Test Address",
                "123-456-7890",
                "Test Description",
                1,  // Assume locationId
                1L  // Assume categoryId
        );

        when(locationRepository.findById(restaurantRequest.getLocationId())).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> restaurantService.registRestaurant(restaurantRequest));

        assertEquals("위치 조회 실패", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void registRestaurant_CategoryNotFound() {
        // given
        RestaurantRequest restaurantRequest = new RestaurantRequest(
                "Test Restaurant",
                "123 Test Address",
                "123-456-7890",
                "Test Description",
                1,  // Assume locationId
                1L  // Assume categoryId
        );

        Location location = Location.createLocation("종로구");

        when(locationRepository.findById(restaurantRequest.getLocationId())).thenReturn(Optional.of(location));
        when(categoryRepository.findById(restaurantRequest.getCategoryId())).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> restaurantService.registRestaurant(restaurantRequest));

        assertEquals("카테고리 조회 실패", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
