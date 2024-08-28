package com.ThreeK_Project.api_server.domain.product.service;

import com.ThreeK_Project.api_server.domain.product.dto.ProductRequest;
import com.ThreeK_Project.api_server.domain.product.dto.ProductResponse;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.repository.ProductRepository;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String createProduct(ProductRequest productRequest, Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }
        // 상품 생성 로직
        Product product = Product.createProduct(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getDescription(),
                restaurant // 상품이 속한 가게를 설정
        );
        productRepository.save(product);

        return "상품 생성 성공";
    }

    public List<ProductResponse> loadProductsByRestaurantId(Restaurant restaurant) {
        // restaurantId를 기준으로 모든 제품을 찾음
        List<Product> products = productRepository.findAllByRestaurant(restaurant);

        // Product 엔티티를 ProductResponse DTO로 변환하여 반환
        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
