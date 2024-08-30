package com.ThreeK_Project.api_server.domain.product.service;

import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.ProductRequestData;
import com.ThreeK_Project.api_server.domain.product.dto.ProductRequest;
import com.ThreeK_Project.api_server.domain.product.dto.ProductResponse;
import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.repository.ProductRepository;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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

    public ProductResponse getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        return new ProductResponse(product);
    }

    @Transactional
    public String updateProduct(UUID productId, ProductRequest productRequest, Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }
        // 상품을 데이터베이스에서 조회, 존재하지 않으면 예외 발생
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 상품이 속한 레스토랑이 일치하는지 확인
        if (!product.getRestaurant().equals(restaurant)) {
            throw new SecurityException("해당 레스토랑에 대한 권한이 없습니다.");
        }

        // Product 엔티티의 필드들을 업데이트
        product.updateProduct(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getDescription()
        );

        return "상품 수정 성공";
    }

    @Transactional
    public String deleteProduct(UUID productId, Restaurant restaurant, User user) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }

        // 상품을 데이터베이스에서 조회, 존재하지 않으면 예외 발생
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 상품이 속한 레스토랑이 일치하는지 확인
        if (!product.getRestaurant().equals(restaurant)) {
            throw new SecurityException("해당 레스토랑에 대한 권한이 없습니다.");
        }

        // 논리적 삭제(기록 후 저장)
        product.deleteBy(user);
        productRepository.save(product);

        return "상품 삭제 성공";
    }

    public List<Product> getProductsByIds(List<ProductRequestData> productRequestDataList) {
        List<UUID> productIds = productRequestDataList.stream()
                .map(ProductRequestData::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new EntityNotFoundException("상품을 찾을 수 없습니다.");
        }
        return products;
    }

    public Product getProductById(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        return product;
    }

}
