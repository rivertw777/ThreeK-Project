package com.ThreeK_Project.api_server.domain.payment.repository;

import com.ThreeK_Project.api_server.domain.payment.dto.RequestDto.PaymentSearchDto;
import com.ThreeK_Project.api_server.domain.payment.entity.Payment;
import com.ThreeK_Project.api_server.domain.payment.enums.PaymentSortType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.ThreeK_Project.api_server.domain.payment.entity.QPayment.payment;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Payment> searchPayments(Pageable pageable, PaymentSearchDto searchDto) {
        List<Payment> content = getPayments(pageable, searchDto);
        Long count = getCount(searchDto);
        return new PageImpl<>(content, pageable, count);
    }

    private Long getCount(PaymentSearchDto searchDTO) {
        return queryFactory
                .select(payment.count())
                .from(payment)
                .where(
                        restaurantNameEq(searchDTO.getRestaurantName()),
                        usernameEq(searchDTO.getUsername()),
                        createdAtBetween(searchDTO.getStartDate(), searchDTO.getEndDate())
                )
                .fetchOne();
    }

    private List<Payment> getPayments(Pageable pageable, PaymentSearchDto searchDTO) {
        return queryFactory
                .selectFrom(payment)
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

    private BooleanExpression restaurantNameEq(String restaurantName) {
        return restaurantName == null ? null : payment.order.restaurant.name.eq(restaurantName);
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : payment.createdBy.username.eq(username);
    }

    private BooleanExpression createdAtBetween(LocalDateTime start, LocalDateTime end) {
        if(start == null || end == null)
            return null;
        return payment.createdAt.between(start, end);
    }

    private OrderSpecifier<?> orderBy(PaymentSortType sortType, Boolean asc) {
        Order inOrder = asc ? Order.ASC: Order.DESC;
        return switch (sortType) {
            case CREATED_AT -> new OrderSpecifier<>(inOrder, payment.createdAt);
            case UPDATED_AT -> new OrderSpecifier<>(inOrder, payment.updatedAt);
        };
    }
}
