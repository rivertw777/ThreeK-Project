package com.ThreeK_Project.api_server.domain.product.repository;

import com.ThreeK_Project.api_server.domain.product.entity.Product;
import com.ThreeK_Project.api_server.domain.product.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Product> searchProductsByKeyword(String keyword, Pageable pageable) {
        QProduct product = QProduct.product;

        BooleanExpression predicate = combinePredicates(keyword, product);

        List<Product> products = queryFactory.selectFrom(product)
                .where(predicate)
                .orderBy(product.createdAt.desc(), product.updatedAt.desc())  // 정렬 조건 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(product)
                .where(predicate)
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }

    private BooleanExpression combinePredicates(String keyword, QProduct product) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        String lowerKeyword = keyword.toLowerCase();
        return product.name.containsIgnoreCase(lowerKeyword)
                .or(product.description.containsIgnoreCase(lowerKeyword))
                .or(product.restaurant.name.containsIgnoreCase(lowerKeyword))
                .or(product.restaurant.description.containsIgnoreCase(lowerKeyword));
    }
}