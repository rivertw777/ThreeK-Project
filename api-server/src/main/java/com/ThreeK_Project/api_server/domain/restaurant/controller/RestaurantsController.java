package com.ThreeK_Project.api_server.domain.restaurant.controller;


import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantRequest;
import com.ThreeK_Project.api_server.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class RestaurantsController {

    private final RestaurantService restaurantService;

    /*
        location과 category를 DB에 미리 추가 해주어야 함
        ex) insert into p_locations(name) values ('종로구');
        ex) insert into p_categories(name) values ('한식');
        ex) location_id와 category_id가 1부터 자동 증가함
     */

    @PostMapping
    public ResponseEntity<String> registRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        // 가게 주인 / 가게 등록
        String result = restaurantService.registRestaurant(restaurantRequest);
        return ResponseEntity.ok().body(result);
    }


}
