package com.nhnacademy.ssacthree_shop_api.commons.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

class QueryDslSortUtilTest {

    @Test
    void parseSort_validSortProperties_shouldReturnOrderSpecifiers() {
        // given
        Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("age"));
        PathBuilder<Object> pathBuilder = new PathBuilder<>(Object.class, "entity");

        // when
        List<OrderSpecifier<?>> orderSpecifiers = QueryDslSortUtil.parseSort(sort, pathBuilder);

        // then
        assertThat(orderSpecifiers).hasSize(2);
        assertThat(orderSpecifiers.get(0).getTarget().toString()).isEqualTo("entity.name");
        assertThat(orderSpecifiers.get(1).getTarget().toString()).isEqualTo("entity.age");
    }


    @Test
    void applyOrderBy_validSort_shouldApplyToQuery() {
        // given
        Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("age"));
        PathBuilder<Object> pathBuilder = new PathBuilder<>(Object.class, "entity");
        JPAQuery<Object> query = new JPAQuery<>();

        // when
        QueryDslSortUtil.applyOrderBy(query, sort, pathBuilder);

        // then
        assertThat(query.toString()).contains("order by entity.name asc, entity.age desc");
    }

    @Test
    void applyOrderBy_emptySort_shouldNotApplyToQuery() {
        // given
        Sort sort = Sort.unsorted();
        PathBuilder<Object> pathBuilder = new PathBuilder<>(Object.class, "entity");
        JPAQuery<Object> query = new JPAQuery<>();

        // when
        QueryDslSortUtil.applyOrderBy(query, sort, pathBuilder);

        // then
        assertThat(query.toString()).doesNotContain("order by");
    }
}
