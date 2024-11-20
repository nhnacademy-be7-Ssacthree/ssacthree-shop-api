package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.QPointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryCustomRepositoryImpl implements PointHistoryCustomRepository {


    private final JPAQueryFactory queryFactory;
    private final QPointHistory qPointHistory = QPointHistory.pointHistory;


    @Override
    public Page<PointHistoryGetResponse> findAllPointHistoryByCustomerId(Long customerId,
        Pageable pageable) {
        List<PointHistoryGetResponse> pointHistoryGetResponseList = queryFactory.select(
                Projections.fields(PointHistoryGetResponse.class,
                    qPointHistory.pointAmount,
                    qPointHistory.pointChangeDate,
                    qPointHistory.pointChangeReason))
            .from(qPointHistory)
            .where(qPointHistory.member.customer.customerId.eq(customerId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(pointHistoryGetResponseList, pageable,
            pointHistoryGetResponseList.size());
    }
}
