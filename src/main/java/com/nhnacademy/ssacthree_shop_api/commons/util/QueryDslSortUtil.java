package com.nhnacademy.ssacthree_shop_api.commons.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryDslSortUtil {
    public static List<OrderSpecifier<?>> parseSort(Sort sort, PathBuilder entityPathBuilder) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC; // 정렬 방향
            String prop = order.getProperty(); // 정렬 대상 필드 가져옴
            try {
                orders.add(new OrderSpecifier<>(direction, entityPathBuilder.get(prop)));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sort property: " + prop, e);
            }
        });

        return orders;
    }

    public static void applyOrderBy(JPAQuery<?> query, Sort sort, PathBuilder entityPathBuilder) {
        List<OrderSpecifier<?>> orderSpecifiers = parseSort(sort, entityPathBuilder);
        if (!orderSpecifiers.isEmpty()) {
            query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        }
    }
}
