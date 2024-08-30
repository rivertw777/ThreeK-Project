package com.ThreeK_Project.api_server.domain.restaurant.entity;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_restaurants")
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID restaurantId;
    // 가게 이름
    private String name;
    private String address;
    private String phoneNumber;
    private String description;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    // 도메인 주도 개발..
    public static Restaurant createRestaurant(
            String name, String address, String phoneNumber, String description,
            User user, Location location, Category category
    ) {
        return Restaurant.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .description(description)
                .user(user)
                .location(location)
                .category(category)
                .build();
    }

    // 수정 메서드 추가
    public void updateRestaurant(
            String name, String address, String phoneNumber, String description,
            Location location, Category category
    ) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("가게 이름은 필수입니다.");
        }
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.location = location;
        this.category = category;
    }
}
