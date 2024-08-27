package com.ThreeK_Project.api_server.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {

    // 가게 이름
    private String name;
    // 가게 주소
    private String address;
    // 가게 전화번호
    private String phoneNumber;
    // 가게 설명
    private String description;
    // 가게 주인 유저 네임
    private String username;
    // 가게 위치 id
    private int locationId;
    // 가게 카테고리 id
    private int categoryId;
}
