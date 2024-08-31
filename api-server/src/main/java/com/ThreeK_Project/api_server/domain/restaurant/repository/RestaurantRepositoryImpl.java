package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.entity.QRestaurant;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RestaurantRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Restaurant> searchRestaurants(String name, String address, String phoneNumber, String description, String username, Integer locationId, Integer categoryId, Pageable pageable) {
        QRestaurant restaurant = QRestaurant.restaurant;

        JPAQuery<Restaurant> query = queryFactory.selectFrom(restaurant)
                .where(
                        name != null ? restaurant.name.containsIgnoreCase(name) : null,
                        address != null ? restaurant.address.containsIgnoreCase(address) : null,
                        phoneNumber != null ? restaurant.phoneNumber.containsIgnoreCase(phoneNumber) : null,
                        description != null ? restaurant.description.containsIgnoreCase(description) : null,
                        username != null ? restaurant.user.username.containsIgnoreCase(username) : null,
                        locationId != null ? restaurant.location.locationId.eq(locationId) : null,
                        categoryId != null ? restaurant.category.categoryId.eq(categoryId) : null
                )
                .orderBy(restaurant.createdAt.desc(), restaurant.updatedAt.desc());  // 정렬 조건 추가

        // 페이징을 적용합니다.
        long total = query.fetchCount();
        List<Restaurant> restaurants = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(restaurants, pageable, total);
    }
}