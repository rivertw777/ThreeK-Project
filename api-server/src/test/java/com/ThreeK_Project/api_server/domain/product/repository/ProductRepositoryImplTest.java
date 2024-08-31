package com.ThreeK_Project.api_server.domain.product.repository;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
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

class ProductRepositoryImplTest {

    @Mock
    private ProductRepositoryImpl productRepository;

    @InjectMocks
    private ProductRepositoryImplTest productRepositoryTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchProductsByKeyword() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product(); // 여기에 Product 객체 초기화 추가
        List<Product> products = Collections.singletonList(product);
        Page<Product> expectedPage = new PageImpl<>(products);

        // When
        when(productRepository.searchProductsByKeyword(any(String.class), any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<Product> result = productRepository.searchProductsByKeyword("찌개", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0)).isEqualTo(product);
    }
}
