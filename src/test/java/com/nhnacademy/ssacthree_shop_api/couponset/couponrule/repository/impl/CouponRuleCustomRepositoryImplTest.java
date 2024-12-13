package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.QCouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
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

class CouponRuleCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private CouponRuleCustomRepositoryImpl couponRuleCustomRepository;

    private static final QCouponRule couponRule = QCouponRule.couponRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCouponRules() {
        // Arrange
        List<CouponRuleGetResponse> mockCouponRules = Arrays.asList(
                new CouponRuleGetResponse(
                        1L,
                        CouponType.RATIO,
                        1000,
                        200,
                        100,
                        "Rule 1",
                        true,
                        LocalDateTime.now()
                ),
                new CouponRuleGetResponse(
                        2L,
                        CouponType.CASH,
                        2000,
                        500,
                        300,
                        "Rule 2",
                        false,
                        LocalDateTime.now().plusDays(1)
                )
        );

        // Mock the query
        JPAQuery<CouponRuleGetResponse> mockedQuery = mock(JPAQuery.class);

        // Configure the query factory to return the mocked query
        when(queryFactory.select(Projections.constructor(
                CouponRuleGetResponse.class,
                couponRule.id,
                couponRule.couponType,
                couponRule.couponMinOrderPrice,
                couponRule.maxDiscountPrice,
                couponRule.couponDiscountPrice,
                couponRule.couponRuleName,
                couponRule.couponIsUsed,
                couponRule.couponRuleCreatedAt
        ))).thenReturn(mockedQuery);

        // Set the behavior for the mocked query
        when(mockedQuery.from(couponRule)).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(couponRule.couponIsUsed.desc())).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(couponRule.couponRuleCreatedAt.asc())).thenReturn(mockedQuery);
        when(mockedQuery.fetch()).thenReturn(mockCouponRules);

        // Act
        List<CouponRuleGetResponse> result = couponRuleCustomRepository.getAllCouponRules();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        CouponRuleGetResponse rule1 = result.get(0);
        assertEquals("Rule 1", rule1.getCouponRuleName());
        assertTrue(rule1.isCouponIsUsed());

        CouponRuleGetResponse rule2 = result.get(1);
        assertEquals("Rule 2", rule2.getCouponRuleName());
        assertFalse(rule2.isCouponIsUsed());

        // QueryFactory 및 메서드 호출 검증
        verify(queryFactory, times(1)).select(any(Expression.class));
        verify(mockedQuery, times(1)).from(couponRule);
        verify(mockedQuery, times(1)).fetch();
    }

    @Test
    void testGetAllSelectedCouponRules() {
        // Arrange
        List<CouponRuleGetResponse> mockCouponRules = Arrays.asList(
                new CouponRuleGetResponse(
                        1L,
                        CouponType.RATIO,
                        1000,
                        200,
                        100,
                        "Rule 1",
                        true,
                        LocalDateTime.now()
                )
        );

        // Mock the query
        JPAQuery<CouponRuleGetResponse> mockedQuery = mock(JPAQuery.class);

        // Configure the query factory to return the mocked query
        when(queryFactory.select(Projections.constructor(
                CouponRuleGetResponse.class,
                couponRule.id,
                couponRule.couponType,
                couponRule.couponMinOrderPrice,
                couponRule.maxDiscountPrice,
                couponRule.couponDiscountPrice,
                couponRule.couponRuleName,
                couponRule.couponIsUsed,
                couponRule.couponRuleCreatedAt
        ))).thenReturn(mockedQuery);

        // Set the behavior for the mocked query
        when(mockedQuery.from(couponRule)).thenReturn(mockedQuery);
        when(mockedQuery.where(couponRule.couponIsUsed.eq(true))).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(couponRule.couponRuleCreatedAt.asc())).thenReturn(mockedQuery);
        when(mockedQuery.fetch()).thenReturn(mockCouponRules);

        // Act
        List<CouponRuleGetResponse> result = couponRuleCustomRepository.getAllSelectedCouponRules();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        CouponRuleGetResponse rule = result.get(0);
        assertEquals("Rule 1", rule.getCouponRuleName());
        assertTrue(rule.isCouponIsUsed());

        // QueryFactory 및 메서드 호출 검증
        verify(queryFactory, times(1)).select(any(Expression.class));
        verify(mockedQuery, times(1)).from(couponRule);
        verify(mockedQuery, times(1)).fetch();
    }
}
