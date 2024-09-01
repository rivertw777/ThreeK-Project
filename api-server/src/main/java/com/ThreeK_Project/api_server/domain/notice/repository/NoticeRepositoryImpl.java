package com.ThreeK_Project.api_server.domain.notice.repository;

import com.ThreeK_Project.api_server.domain.notice.dto.RequestDto.NoticeSearchDto;
import com.ThreeK_Project.api_server.domain.notice.entity.Notice;
import com.ThreeK_Project.api_server.domain.notice.enums.NoticeSortType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ThreeK_Project.api_server.domain.notice.entity.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notice> searchNotices(Pageable pageable, NoticeSearchDto searchDto) {
        List<Notice> notices = getNoticeList(pageable, searchDto);
        Long count = getCount(searchDto);

        return new PageImpl<>(notices, pageable, count);
    }

    private Long getCount(NoticeSearchDto searchDTO) {
        return queryFactory
                .select(notice.count())
                .from(notice)
                .where(
                        titleAndContentContains(searchDTO.getKeyword())
                )
                .fetchOne();
    }

    private List<Notice> getNoticeList(Pageable pageable, NoticeSearchDto searchDTO) {
        return queryFactory
                .selectFrom(notice)
                .where(
                        titleAndContentContains(searchDTO.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderBy(searchDTO.getSortBy(), searchDTO.getAscending()))
                .fetch();
    }

    private BooleanExpression titleAndContentContains(String keyword) {
        return keyword == null ? null : notice.title.contains(keyword).or(notice.content.contains(keyword));
    }

    private OrderSpecifier<?> orderBy(NoticeSortType sortType, Boolean asc) {
        Order inOrder = asc ? Order.ASC: Order.DESC;
        return switch (sortType) {
            case CREATED_AT -> new OrderSpecifier<>(inOrder, notice.createdAt);
            case UPDATED_AT -> new OrderSpecifier<>(inOrder, notice.updatedAt);
        };
    }
}
