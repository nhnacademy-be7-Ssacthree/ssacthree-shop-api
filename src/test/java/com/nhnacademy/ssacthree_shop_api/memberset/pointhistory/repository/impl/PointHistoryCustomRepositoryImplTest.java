package com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.QPointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.dto.PointHistoryGetResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class PointHistoryCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private PointHistoryCustomRepositoryImpl pointHistoryCustomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllPointHistoryByCustomerId() {
        // Given
        Long customerId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        PointHistoryGetResponse response = new PointHistoryGetResponse(100, null, "Purchase");
        List<PointHistoryGetResponse> mockResult = Collections.singletonList(response);

        JPAQuery<PointHistoryGetResponse> mockQuery = mock(JPAQuery.class);

        when(queryFactory.select(
            Projections.fields(PointHistoryGetResponse.class,
                QPointHistory.pointHistory.pointAmount,
                QPointHistory.pointHistory.pointChangeDate,
                QPointHistory.pointHistory.pointChangeReason)))
            .thenReturn(mockQuery);

        when(mockQuery.from(QPointHistory.pointHistory))
            .thenReturn(mockQuery);

        when(mockQuery.where(QPointHistory.pointHistory.member.customer.customerId.eq(customerId)))
            .thenReturn(mockQuery);

        when(mockQuery.offset(pageable.getOffset()))
            .thenReturn(mockQuery);

        when(mockQuery.limit(pageable.getPageSize()))
            .thenReturn(mockQuery);

        when(mockQuery.fetch())
            .thenReturn(mockResult);

        // When
        Page<PointHistoryGetResponse> result = pointHistoryCustomRepository.findAllPointHistoryByCustomerId(
            customerId, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getPointAmount()).isEqualTo(100);

        verify(queryFactory).select(
            Projections.fields(PointHistoryGetResponse.class,
                QPointHistory.pointHistory.pointAmount,
                QPointHistory.pointHistory.pointChangeDate,
                QPointHistory.pointHistory.pointChangeReason));
        verify(mockQuery).from(QPointHistory.pointHistory);
        verify(mockQuery).where(
            QPointHistory.pointHistory.member.customer.customerId.eq(customerId));
        verify(mockQuery).offset(pageable.getOffset());
        verify(mockQuery).limit(pageable.getPageSize());
        verify(mockQuery).fetch();
    }

}