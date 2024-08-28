package com.ThreeK_Project.api_server.domain.product.service;

import com.ThreeK_Project.api_server.domain.product.dto.ProductRequest;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.repository.ProductRepository;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String createProduct(ProductRequest productRequest, Restaurant restaurant) {

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
}
