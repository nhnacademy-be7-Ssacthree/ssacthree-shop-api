package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.impl;

import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.QPointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.dto.PointSaveRuleGetResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointSaveRuleCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private PointSaveRuleCustomRepositoryImpl pointSaveRuleCustomRepository;

    private static final QPointSaveRule pointSaveRule = QPointSaveRule.pointSaveRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPointSaveRules() {
        // Arrange
        List<PointSaveRuleGetResponse> mockPointSaveRules = Arrays.asList(
                new PointSaveRuleGetResponse(
                        1L,
                        "Rule 1",
                        500,
                        LocalDateTime.now(),
                        true,
                        PointSaveType.PERCENT
                ),
                new PointSaveRuleGetResponse(
                        2L,
                        "Rule 2",
                        1000,
                        LocalDateTime.now().plusDays(1),
                        false,
                        PointSaveType.INTEGER
                )
        );

        // Mock the query
        JPAQuery<PointSaveRuleGetResponse> mockedQuery = mock(JPAQuery.class);

        // Configure the query factory to return the mocked query
        when(queryFactory.select(Projections.constructor(
                PointSaveRuleGetResponse.class,
                pointSaveRule.pointSaveRuleId,
                pointSaveRule.pointSaveRuleName,
                pointSaveRule.pointSaveAmount,
                pointSaveRule.pointSaveRuleGenerateDate,
                pointSaveRule.pointSaveRuleIsSelected,
                pointSaveRule.pointSaveType
        ))).thenReturn(mockedQuery);

        // Set the behavior for the mocked query
        when(mockedQuery.from(pointSaveRule)).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(pointSaveRule.pointSaveRuleIsSelected.desc())).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(pointSaveRule.pointSaveRuleGenerateDate.asc())).thenReturn(mockedQuery);
        when(mockedQuery.fetch()).thenReturn(mockPointSaveRules);

        // Act
        List<PointSaveRuleGetResponse> result = pointSaveRuleCustomRepository.getAllPointSaveRules();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        PointSaveRuleGetResponse rule1 = result.get(0);
        assertEquals("Rule 1", rule1.getPointSaveRuleName());
        assertTrue(rule1.isPointSaveRuleIsSelected());
        assertEquals(PointSaveType.PERCENT, rule1.getPointSaveType());

        PointSaveRuleGetResponse rule2 = result.get(1);
        assertEquals("Rule 2", rule2.getPointSaveRuleName());
        assertFalse(rule2.isPointSaveRuleIsSelected());
        assertEquals(PointSaveType.INTEGER, rule2.getPointSaveType());

        // QueryFactory 및 메서드 호출 검증
        verify(queryFactory, times(1)).select((Expression<Object>) any());
        verify(mockedQuery, times(1)).from(pointSaveRule);
        verify(mockedQuery, times(1)).fetch();
    }
}
