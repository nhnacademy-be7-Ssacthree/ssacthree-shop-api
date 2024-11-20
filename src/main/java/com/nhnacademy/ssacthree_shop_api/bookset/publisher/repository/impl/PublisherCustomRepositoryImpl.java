package com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherCustomRepository;
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
public class PublisherCustomRepositoryImpl implements PublisherCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QPublisher publisher = QPublisher.publisher;

    @Override
    public Page<PublisherGetResponse> findAllPublisher(Pageable pageable) {
        List<PublisherGetResponse> publishers = queryFactory.select(Projections.constructor(
                PublisherGetResponse.class,
                publisher.publisherId,
                publisher.publisherName,
                publisher.publisherIsUsed))
                .from(publisher)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(publisher.count())
                .from(publisher)
                .fetchOne();

        count = (count == null) ? 0 : count;

        return new PageImpl<>(publishers, pageable, count);
    }

    @Override
    public List<PublisherGetResponse> findAllPublisherList(){
        return queryFactory.select(Projections.constructor(
                        PublisherGetResponse.class,
                        publisher.publisherId,
                        publisher.publisherName,
                        publisher.publisherIsUsed))
                .from(publisher)
                .fetch();
    }
}
