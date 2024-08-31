package com.ThreeK_Project.api_server.domain.restaurant.controller;

import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderSearchDTO;
import com.ThreeK_Project.api_server.domain.order.dto.ResponseDto.OrderResponseDto;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.domain.product.dto.ProductRequest;
import com.ThreeK_Project.api_server.domain.product.service.ProductService;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.service.RestaurantService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/stores")
public class RestaurantsOwnerController {

    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final OrderService orderService;

    /*
        location과 category를 DB에 미리 추가 해주어야 함
        ex) insert into p_locations(name) values ('종로구');
        ex) insert into p_categories(name) values ('한식');
        ex) location_id와 category_id가 1부터 자동 증가함
     */

    @Operation(summary = "가게 주인 / 가게 등록(role: OWNER 이상)")
    @PostMapping
    public ResponseEntity<String> registRestaurant(@RequestBody RestaurantRequest restaurantRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        String result = restaurantService.registRestaurant(restaurantRequest, user);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 주인 / 단일 수정(role: OWNER 이상)")
    @PutMapping("/{restaurantId}")
    public ResponseEntity<String> updateRestaurant(@RequestBody RestaurantRequest restaurantRequest,
                                                   @PathVariable UUID restaurantId,
                                                   @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        String result = restaurantService.updateRestaurant(restaurantRequest, restaurantId, user);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 주인 / 단일 삭제(role: OWNER 이상)")
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Map<String, String>> deleteRestaurant(@PathVariable UUID restaurantId,
                                                                @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        String result = restaurantService.deleteRestaurant(restaurantId, user);
        return ResponseEntity.ok().body(Map.of("message", result));
    }

    // Product 관련 컨트롤러
    @Operation(summary = "가게 주인 / 상품 생성(role: OWNER 이상)")
    @PostMapping("/{restaurantId}/products")
    public ResponseEntity<Map<String, String>> createProduct(@PathVariable UUID restaurantId,
                                                             @RequestBody ProductRequest productRequest,
                                                             @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        Restaurant restaurant = restaurantService.validateAndGetRestaurant(restaurantId, user);
        String result = productService.createProduct(productRequest, restaurant);
        return ResponseEntity.ok().body(Map.of("message", result));
    }

    @Operation(summary = "가게 주인 / 상품 단일 수정")
    @PutMapping("/{restaurantId}/products/{productId}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable UUID restaurantId,
                                                             @PathVariable UUID productId,
                                                             @RequestBody ProductRequest productRequest,
                                                             @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        Restaurant restaurant = restaurantService.validateAndGetRestaurant(restaurantId, user);
        String result = productService.updateProduct(productId, productRequest, restaurant);
        return ResponseEntity.ok().body(Map.of("message", result));
    }

    @Operation(summary = "가게 주인 / 상품 단일 삭제")
    @DeleteMapping("/{restaurantId}/products/{productId}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable UUID restaurantId,
                                                             @PathVariable UUID productId,
                                                             @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        Restaurant restaurant = restaurantService.validateAndGetRestaurant(restaurantId, user);
        String result = productService.deleteProduct(productId, restaurant, user);
        return ResponseEntity.ok().body(Map.of("message", result));
    }

    @Operation(summary = "가게 주인 / 주문 검색")
    @GetMapping("{restaurantId}/orders/search")
    public Page<OrderResponseDto> searchRestaurantOrders(@PathVariable UUID restaurantId, @ModelAttribute OrderSearchDTO orderSearchDTO,
                                                         @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userDetailsCustom.getUser();
        String restaurantName = restaurantService.validateAndGetRestaurant(restaurantId, user).getName();
        return orderService.searchRestaurantOrders(restaurantName, orderSearchDTO);
    }
}
