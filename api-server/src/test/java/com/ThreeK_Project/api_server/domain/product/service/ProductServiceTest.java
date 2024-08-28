package com.ThreeK_Project.api_server.domain.product.service;

import com.ThreeK_Project.api_server.domain.product.dto.ProductRequest;
import com.ThreeK_Project.api_server.domain.product.dto.ProductResponse;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.repository.ProductRepository;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.restaurant.service.RestaurantService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @InjectMocks
    private ProductService productService;

    @Mock
    private User mockUser;

    @Mock
    private Restaurant mockRestaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // User Mock 설정
        when(mockUser.getUsername()).thenReturn("testUser");

        // Restaurant Mock 설정
        when(mockRestaurant.getUser()).thenReturn(mockUser);
    }

    @Test
    void testValidateAndGetRestaurant_Success() {
        UUID restaurantId = UUID.randomUUID();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));

        Restaurant restaurant = restaurantService.validateAndGetRestaurant(restaurantId, mockUser);

        assertNotNull(restaurant);
        assertEquals(mockRestaurant, restaurant);
        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    @Test
    void testValidateAndGetRestaurant_NotFound() {
        UUID restaurantId = UUID.randomUUID();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                restaurantService.validateAndGetRestaurant(restaurantId, mockUser));

        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    @Test
    void testValidateAndGetRestaurant_AccessDenied() {
        UUID restaurantId = UUID.randomUUID();
        User otherUser = mock(User.class); // 다른 유저 목 생성
        when(otherUser.getUsername()).thenReturn("otherUser");

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(mockRestaurant));

        assertThrows(SecurityException.class, () ->
                restaurantService.validateAndGetRestaurant(restaurantId, otherUser));

        verify(restaurantRepository, times(1)).findById(restaurantId);
    }

    @Test
    void testCreateProduct_Success() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Product Name");
        productRequest.setPrice(1000);
        productRequest.setDescription("Product Description");

        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        String result = productService.createProduct(productRequest, mockRestaurant);

        assertEquals("상품 생성 성공", result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_NullRestaurant() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Product Name");
        productRequest.setPrice(1000);
        productRequest.setDescription("Product Description");

        // 테스트에서 예외를 기대하기 때문에 IllegalArgumentException이 발생해야 함
        assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(productRequest, null);
        });

        // ProductRepository가 save를 호출하지 않았는지 검증
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testLoadProductsByRestaurantId_Success() {
        when(productRepository.findAllByRestaurant(mockRestaurant))
                .thenReturn(Arrays.asList(
                        Product.createProduct("Product1", 1000, "Description1", mockRestaurant),
                        Product.createProduct("Product2", 2000, "Description2", mockRestaurant)
                ));

        List<ProductResponse> productResponses = productService.loadProductsByRestaurantId(mockRestaurant);

        assertNotNull(productResponses);
        assertEquals(2, productResponses.size());
        assertEquals("Product1", productResponses.get(0).getName());
        assertEquals("Product2", productResponses.get(1).getName());

        verify(productRepository, times(1)).findAllByRestaurant(mockRestaurant);
    }

    @Test
    void testLoadProductsByRestaurantId_Empty() {
        when(productRepository.findAllByRestaurant(mockRestaurant)).thenReturn(Arrays.asList());

        List<ProductResponse> productResponses = productService.loadProductsByRestaurantId(mockRestaurant);

        assertNotNull(productResponses);
        assertTrue(productResponses.isEmpty());

        verify(productRepository, times(1)).findAllByRestaurant(mockRestaurant);
    }

    @Test
    void testGetProduct_Success() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        // Create a Product with a set productId
        Product product = Product.createProduct("Test Product", 1000, "Test Description", restaurant);
        ReflectionTestUtils.setField(product, "productId", productId); // force set the productId

        when(restaurant.getRestaurantId()).thenReturn(UUID.randomUUID());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.getProduct(productId);

        assertNotNull(productResponse);
        assertEquals(productId, productResponse.getProductId());
        assertEquals("Test Product", productResponse.getName());
        assertEquals("1000", productResponse.getPrice());
        assertEquals("Test Description", productResponse.getDescription());
        verify(productRepository, times(1)).findById(productId);
    }


    @Test
    void testGetProduct_NotFound() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProduct(productId));
        verify(productRepository, times(1)).findById(productId);
    }

}
