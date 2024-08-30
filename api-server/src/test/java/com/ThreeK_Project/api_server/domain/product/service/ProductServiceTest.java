package com.ThreeK_Project.api_server.domain.product.service;

import com.ThreeK_Project.api_server.domain.order.dto.ProductRequestData;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


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
        assertEquals(1000, productResponse.getPrice());
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

    @Test
    void testUpdateProduct_Success() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        // 기존 상품을 설정
        Product existingProduct = Product.createProduct("Old Product", 1000, "Old Description", restaurant);
        ReflectionTestUtils.setField(existingProduct, "productId", productId); // force set the productId

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product Name");
        productRequest.setPrice(2000);
        productRequest.setDescription("Updated Description");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        String result = productService.updateProduct(productId, productRequest, restaurant);

        assertEquals("상품 수정 성공", result);
        assertEquals("Updated Product Name", existingProduct.getName());
        assertEquals(2000, existingProduct.getPrice());
        assertEquals("Updated Description", existingProduct.getDescription());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdateProduct_NotFound() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product Name");
        productRequest.setPrice(2000);
        productRequest.setDescription("Updated Description");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                productService.updateProduct(productId, productRequest, restaurant));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdateProduct_RestaurantMismatch() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        Restaurant otherRestaurant = mock(Restaurant.class); // 다른 레스토랑

        // 기존 상품을 설정
        Product existingProduct = Product.createProduct("Old Product", 1000, "Old Description", restaurant);
        ReflectionTestUtils.setField(existingProduct, "productId", productId); // force set the productId

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product Name");
        productRequest.setPrice(2000);
        productRequest.setDescription("Updated Description");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        assertThrows(SecurityException.class, () ->
                productService.updateProduct(productId, productRequest, otherRestaurant));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdateProduct_RestaurantIsNull() {
        UUID productId = UUID.randomUUID();

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product Name");
        productRequest.setPrice(2000);
        productRequest.setDescription("Updated Description");

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(productId, productRequest, null));
        verify(productRepository, never()).findById(any(UUID.class));
    }

    @Test
    void testDeleteProduct_Success() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        // 기존 상품을 설정
        Product existingProduct = spy(Product.createProduct("Test Product", 1000, "Test Description", restaurant));
        ReflectionTestUtils.setField(existingProduct, "productId", productId); // force set the productId

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        String result = productService.deleteProduct(productId, restaurant, mockUser);

        assertEquals("상품 삭제 성공", result);
        verify(existingProduct, times(1)).deleteBy(mockUser); // 논리적 삭제가 호출되었는지 확인
        verify(productRepository, times(1)).save(existingProduct); // 논리적 삭제 후 save가 호출되어야 함
    }

    @Test
    void testDeleteProduct_NotFound() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                productService.deleteProduct(productId, restaurant, mockUser));
        verify(productRepository, never()).save(any(Product.class)); // 논리적 삭제가 호출되지 않았는지 확인
    }

    @Test
    void testDeleteProduct_RestaurantMismatch() {
        UUID productId = UUID.randomUUID();
        Restaurant restaurant = mock(Restaurant.class);
        Restaurant otherRestaurant = mock(Restaurant.class); // 다른 레스토랑

        // 기존 상품을 설정
        Product existingProduct = Product.createProduct("Test Product", 1000, "Test Description", restaurant);
        ReflectionTestUtils.setField(existingProduct, "productId", productId); // force set the productId

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        assertThrows(SecurityException.class, () ->
                productService.deleteProduct(productId, otherRestaurant, mockUser));
        verify(productRepository, never()).save(any(Product.class)); // 논리적 삭제가 호출되지 않았는지 확인
    }

    @Test
    void testDeleteProduct_RestaurantIsNull() {
        UUID productId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                productService.deleteProduct(productId, null, mockUser));
        verify(productRepository, never()).findById(any(UUID.class));
    }
    @Test
    void testGetProductsByIds_Success() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(productRepository.findAllById(Arrays.asList(productId1, productId2)))
                .thenReturn(Arrays.asList(product1, product2));

        List<ProductRequestData> productRequestDataList = Arrays.asList(
                new ProductRequestData(productId1, 1, null),
                new ProductRequestData(productId2, 1, null)
        );

        List<Product> products = productService.getProductsByIds(productRequestDataList);

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAllById(Arrays.asList(productId1, productId2));
    }

    @Test
    void testGetProductsByIds_PartialNotFound() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Product product1 = mock(Product.class);

        when(productRepository.findAllById(Arrays.asList(productId1, productId2)))
                .thenReturn(Arrays.asList(product1));  // productId2가 누락됨

        List<ProductRequestData> productRequestDataList = Arrays.asList(
                new ProductRequestData(productId1, 1, null),
                new ProductRequestData(productId2, 1, null)
        );

        assertThrows(EntityNotFoundException.class, () ->
                productService.getProductsByIds(productRequestDataList)
        );

        verify(productRepository, times(1)).findAllById(Arrays.asList(productId1, productId2));
    }

    @Test
    void testGetProductsByIds_EmptyList() {
        List<ProductRequestData> productRequestDataList = Arrays.asList();

        List<Product> products = productService.getProductsByIds(productRequestDataList);

        assertNotNull(products);
        assertTrue(products.isEmpty());
        verify(productRepository, times(1)).findAllById(Arrays.asList());
    }

    @Test
    void testSearchProduct() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Restaurant restaurant = new Restaurant(); // Assuming you have a builder for Restaurant
        Product product = Product.createProduct("Test Product", 1000, "Test Description", restaurant);

        List<Product> products = Collections.singletonList(product);
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.searchProductsByKeyword(any(String.class), any(Pageable.class)))
                .thenReturn(productPage);

        // When
        Page<ProductResponse> result = productService.searchProduct("testKeyword", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Product");
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(1000);
        assertThat(result.getContent().get(0).getDescription()).isEqualTo("Test Description");
    }

}
