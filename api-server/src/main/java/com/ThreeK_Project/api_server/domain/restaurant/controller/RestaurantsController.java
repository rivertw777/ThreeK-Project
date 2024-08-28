package com.ThreeK_Project.api_server.domain.restaurant.controller;


import com.ThreeK_Project.api_server.domain.product.dto.ProductRequest;
import com.ThreeK_Project.api_server.domain.product.service.ProductService;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantResponse;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.ThreeK_Project.api_server.domain.restaurant.repository.RestaurantRepository;
import com.ThreeK_Project.api_server.domain.restaurant.service.RestaurantService;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.security.auth.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class RestaurantsController {

    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;
    private final ProductService productService;

    /*
        location과 category를 DB에 미리 추가 해주어야 함
        ex) insert into p_locations(name) values ('종로구');
        ex) insert into p_categories(name) values ('한식');
        ex) location_id와 category_id가 1부터 자동 증가함
     */


    @PostMapping
    public ResponseEntity<String> registRestaurant(@RequestBody RestaurantRequest restaurantRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        // 가게 주인 / 가게 등록(role: OWNER 이상)
        User user = userDetailsCustom.getUser();
        String result = restaurantService.registRestaurant(restaurantRequest, user);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAllRestaurant() {
        // 가게 전체 조회(role: x)
        List<RestaurantResponse> resultList = restaurantService.findAllRestaurant();
        return ResponseEntity.ok().body(resultList);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> findRestaurantById(@PathVariable UUID restaurantId) {
        // 가게 단일 조회(role: x)
        RestaurantResponse result = restaurantService.findRestaurantById(restaurantId);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<String> updateRestaurant(@RequestBody RestaurantRequest restaurantRequest,
                                                   @PathVariable UUID restaurantId,
                                                   @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        // 가게 주인 / 단일 수정(role: OWNER 이상)
        User user = userDetailsCustom.getUser();
        String result = restaurantService.updateRestaurant(restaurantRequest, restaurantId, user);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Map<String, String>> deleteRestaurant(@PathVariable UUID restaurantId,
                                                   @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        // 가게 주인 / 단일 삭제(role: OWNER 이상)
        User user = userDetailsCustom.getUser();
        String result = restaurantService.deleteRestaurant(restaurantId, user);
        return ResponseEntity.ok().body(Map.of("message", result));
    }

    // Product 관련 컨트롤러
    @PostMapping("/{restaurantId}/products")
    public ResponseEntity<Map<String, String>> createProduct(@PathVariable UUID restaurantId,
                                                             @RequestBody ProductRequest productRequest,
                                                             @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        // 가게 주인 / 상품 생성(role: OWNER 이상)
        User user = userDetailsCustom.getUser();
        Restaurant restaurant= restaurantService.validateAndGetRestaurant(restaurantId, user);
        String result = productService.createProduct(productRequest, restaurant);
        return ResponseEntity.ok().body(Map.of("message", result));
    }
























}
