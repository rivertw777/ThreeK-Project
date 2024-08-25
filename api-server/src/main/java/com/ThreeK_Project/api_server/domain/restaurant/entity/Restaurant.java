package com.ThreeK_Project.api_server.domain.restaurant.entity;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.user.entity.User;
import com.ThreeK_Project.api_server.global.audit.BaseEntity;
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
    private String name;
    private String address;
    private String phoneNumber;
    private String description;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_name")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static Restaurant createRestaurant(
            String name, String address, String phoneNumber, String description,
            Location location, Category category
    ) {
        return Restaurant.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .description(description)
                .location(location)
                .category(category)
                .build();
    }
}
