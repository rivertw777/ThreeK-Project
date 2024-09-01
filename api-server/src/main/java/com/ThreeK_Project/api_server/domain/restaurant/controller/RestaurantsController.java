package com.ThreeK_Project.api_server.domain.restaurant.controller;


import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderRequestDto;
import com.ThreeK_Project.api_server.domain.order.service.OrderService;
import com.ThreeK_Project.api_server.domain.product.dto.ProductResponse;
import com.ThreeK_Project.api_server.domain.product.service.ProductService;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class RestaurantsController {

    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final OrderService orderService;

    /*
        location과 category를 DB에 미리 추가 해주어야 함
        ex) insert into p_locations(name) values ('종로구');
        ex) insert into p_categories(name) values ('한식');
        ex) location_id와 category_id가 1부터 자동 증가함
     */

    @Operation(summary = "가게 전체 조회(role: x)")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAllRestaurant() {
        List<RestaurantResponse> resultList = restaurantService.findAllRestaurant();
        return ResponseEntity.ok().body(resultList);
    }

    @Operation(summary = "가게 단일 조회(role: x)")
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> findRestaurantById(@PathVariable UUID restaurantId) {
        RestaurantResponse result = restaurantService.findRestaurantById(restaurantId);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "가게 상품 전체 조회(role: x)")
    @GetMapping("/{restaurantId}/products")
    public ResponseEntity<List<ProductResponse>> searchProductsByRestaurantId(@PathVariable UUID restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        List<ProductResponse> productResponseList  = productService.loadProductsByRestaurantId(restaurant);
        return ResponseEntity.ok().body(productResponseList);
    }

    // Order 관련 컨트롤러
    @Operation(summary = "사용자 / 주문 생성")
    @PostMapping("/{restaurantId}/orders")
    public ResponseEntity<Map<String, String>> createOrder(@PathVariable UUID restaurantId,
                                                           @RequestBody @Valid OrderRequestDto orderRequestDto) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        String result = orderService.createOrder(orderRequestDto, restaurant);

        return ResponseEntity.ok().body(Map.of("message", result));
    }

    @Operation(summary = "가게 검색")
    @GetMapping("/search")
    public Page<RestaurantResponse> searchRestaurants(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) Integer categoryId,
            @PageableDefault(size = 10)
            Pageable pageable) {

        return restaurantService.searchRestaurants(name, address, phoneNumber, description, username, locationId, categoryId, pageable);
    }

}
