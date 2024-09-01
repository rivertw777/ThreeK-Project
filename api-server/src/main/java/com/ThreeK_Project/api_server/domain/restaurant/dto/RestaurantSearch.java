package com.ThreeK_Project.api_server.domain.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantSearch {
    private String name;
    private String address;
    private String phoneNumber;
    private String description;
    private String username;
    private Integer locationId;
    private Integer categoryId;
}
