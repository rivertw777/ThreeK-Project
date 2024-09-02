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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        validateRestaurant(restaurant);
        Product product = buildProductFromRequest(productRequest, restaurant);
        productRepository.save(product);

        return "상품 생성 성공";
    }

    public List<ProductResponse> loadProductsByRestaurantId(Restaurant restaurant) {
        List<Product> products = productRepository.findAllByRestaurant(restaurant);
        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public ProductResponse getProduct(UUID productId) {
        Product product = findProductById(productId);
        return new ProductResponse(product);
    }

    @Transactional
    public String updateProduct(UUID productId, ProductRequest productRequest, Restaurant restaurant) {
        validateRestaurant(restaurant);
        Product product = validateProductOwnership(productId, restaurant);

        product.updateProduct(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getDescription()
        );

        return "상품 수정 성공";
    }

    @Transactional
    public String deleteProduct(UUID productId, Restaurant restaurant, User user) {
        validateRestaurant(restaurant);
        Product product = validateProductOwnership(productId, restaurant);

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
            throw new EntityNotFoundException("요청한 상품 중 일부를 찾을 수 없습니다.");
        }

        return products;
    }

    public Product getProductById(UUID productId) {
        return findProductById(productId);
    }

    public Page<ProductResponse> searchProduct(String keyword, Pageable pageable) {
        return productRepository.searchProductsByKeyword(keyword, pageable)
                .map(ProductResponse::new);
    }

    // Helper methods

    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant cannot be null");
        }
    }

    private Product validateProductOwnership(UUID productId, Restaurant restaurant) {
        Product product = findProductById(productId);
        if (!product.getRestaurant().equals(restaurant)) {
            throw new SecurityException("해당 레스토랑에 대한 권한이 없습니다.");
        }
        return product;
    }

    private Product buildProductFromRequest(ProductRequest productRequest, Restaurant restaurant) {
        return Product.createProduct(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getDescription(),
                restaurant
        );
    }
}
