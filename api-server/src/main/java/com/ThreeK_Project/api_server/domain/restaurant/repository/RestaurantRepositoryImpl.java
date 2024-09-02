package com.ThreeK_Project.api_server.domain.restaurant.repository;

import com.ThreeK_Project.api_server.domain.restaurant.dto.RestaurantSearch;
import com.ThreeK_Project.api_server.domain.restaurant.entity.QRestaurant;
import com.ThreeK_Project.api_server.domain.restaurant.entity.Restaurant;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RestaurantRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Restaurant> searchRestaurants(RestaurantSearch search, Pageable pageable) {
        QRestaurant restaurant = QRestaurant.restaurant;

        BooleanBuilder whereClause = buildWhereClause(search, restaurant);

        JPAQuery<Restaurant> query = queryFactory.selectFrom(restaurant)
                .where(whereClause)
                .orderBy(restaurant.createdAt.desc(), restaurant.updatedAt.desc());  // 정렬 조건 추가

        // 페이징을 적용합니다.
        long total = query.fetchCount();
        List<Restaurant> restaurants = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(restaurants, pageable, total);
    }

    private BooleanBuilder buildWhereClause(RestaurantSearch search, QRestaurant restaurant) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (search.getName() != null) {
            whereClause.and(restaurant.name.containsIgnoreCase(search.getName()));
        }
        if (search.getAddress() != null) {
            whereClause.and(restaurant.address.containsIgnoreCase(search.getAddress()));
        }
        if (search.getPhoneNumber() != null) {
            whereClause.and(restaurant.phoneNumber.containsIgnoreCase(search.getPhoneNumber()));
        }
        if (search.getDescription() != null) {
            whereClause.and(restaurant.description.containsIgnoreCase(search.getDescription()));
        }
        if (search.getUsername() != null) {
            whereClause.and(restaurant.user.username.containsIgnoreCase(search.getUsername()));
        }
        if (search.getLocationId() != null) {
            whereClause.and(restaurant.location.locationId.eq(search.getLocationId()));
        }
        if (search.getCategoryId() != null) {
            whereClause.and(restaurant.category.categoryId.eq(search.getCategoryId()));
        }

        return whereClause;
    }
}
