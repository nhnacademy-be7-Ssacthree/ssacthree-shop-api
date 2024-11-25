package com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagCustomRepositoryImpl implements TagCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QTag tag = QTag.tag;


    @Override
    public Page<TagInfoResponse> findAllTags(Pageable pageable) {
        List<TagInfoResponse> tags = queryFactory.select(Projections.constructor(
                TagInfoResponse.class,
                tag.tagId,
                tag.tagName))
                .from(tag)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(tag.count())
                .from(tag)
                .fetchOne();

        count = (count == null) ? 0 : count;

        return new PageImpl<>(tags, pageable, count);
    }

    @Override
    public List<TagInfoResponse> findAllTagList() {
        return queryFactory.select(Projections.constructor(
                TagInfoResponse.class,
                tag.tagId,
                tag.tagName))
            .from(tag)
            .fetch();
    }
}
