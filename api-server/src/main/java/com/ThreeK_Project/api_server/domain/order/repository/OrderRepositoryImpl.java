package com.ThreeK_Project.api_server.domain.order.repository;

import com.ThreeK_Project.api_server.domain.order.dto.RequestDto.OrderSearchDTO;
import com.ThreeK_Project.api_server.domain.order.entity.Order;
import com.ThreeK_Project.api_server.domain.order.enums.OrderSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.ThreeK_Project.api_server.domain.order.entity.QOrder.order;
import static com.ThreeK_Project.api_server.domain.order.entity.QOrderProduct.orderProduct;
import static com.ThreeK_Project.api_server.domain.payment.entity.QPayment.payment;
import static com.ThreeK_Project.api_server.domain.product.entity.QProduct.product;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(Pageable pageable, OrderSearchDTO searchDTO) {
        List<UUID> orderIds = getIdList(pageable, searchDTO);

        List<Order> content = getOrderResponseDto(orderIds, searchDTO);

        Long count = getCount(searchDTO);

        return new PageImpl<>(content, pageable, count);
    }

    private Long getCount(OrderSearchDTO searchDTO) {
        return queryFactory
                .select(order.count())
                .from(order)
                .where(
                        restaurantNameEq(searchDTO.getRestaurantName()),
                        usernameEq(searchDTO.getUsername()),
                        createdAtBetween(searchDTO.getStartDate(), searchDTO.getEndDate())
                )
                .fetchOne();
    }

    private List<UUID> getIdList(Pageable pageable, OrderSearchDTO searchDTO) {
        return queryFactory
                .select(order.orderId)
                .from(order)
                .where(
                        restaurantNameEq(searchDTO.getRestaurantName()),
                        usernameEq(searchDTO.getUsername()),
                        createdAtBetween(searchDTO.getStartDate(), searchDTO.getEndDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderBy(searchDTO.getSortBy(), searchDTO.getAscending()))
                .fetch();
    }

    private List<Order> getOrderResponseDto(List<UUID> ids, OrderSearchDTO searchDTO) {
         return queryFactory
                 .selectFrom(order)
                 .leftJoin(order.orderProducts, orderProduct).fetchJoin()
                 .leftJoin(orderProduct.product, product).fetchJoin()
                 .leftJoin(order.payment, payment).fetchJoin()
                 .where(order.orderId.in(ids))
                 .orderBy(orderBy(searchDTO.getSortBy(), searchDTO.getAscending()))
                 .fetch();
    }

    private BooleanExpression restaurantNameEq(String restaurantName) {
        return restaurantName == null ? null : order.restaurant.name.eq(restaurantName);
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : order.createdBy.username.eq(username);
    }

    private BooleanExpression createdAtBetween(LocalDateTime start, LocalDateTime end) {
        if(start == null || end == null)
            return null;
        return order.createdAt.between(start, end);
    }

    private OrderSpecifier<?> orderBy(OrderSortType sortType, Boolean asc) {
        com.querydsl.core.types.Order inOrder = asc ? com.querydsl.core.types.Order.ASC: com.querydsl.core.types.Order.DESC;
        return switch (sortType) {
            case CREATED_AT -> new OrderSpecifier<>(inOrder, order.createdAt);
            case UPDATED_AT -> new OrderSpecifier<>(inOrder, order.updatedAt);
        };
    }

}
