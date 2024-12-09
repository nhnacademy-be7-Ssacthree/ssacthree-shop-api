package com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.CouponEffectivePeriodUnit;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.QCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
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

class CouponCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private CouponCustomRepositoryImpl couponCustomRepository;

    private static final QCoupon coupon = QCoupon.coupon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCoupons() {
        // Arrange
        List<CouponGetResponse> mockCoupons = Arrays.asList(
                new CouponGetResponse(
                        1L,
                        "Coupon 1",
                        "Description for Coupon 1",
                        10,
                        CouponEffectivePeriodUnit.DAY,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(10),
                        100L),
                new CouponGetResponse(
                        2L,
                        "Coupon 2",
                        "Description for Coupon 2",
                        5,
                        CouponEffectivePeriodUnit.MONTH,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMonths(1),
                        101L)
        );

        // Mock the query
        JPAQuery<CouponGetResponse> mockedQuery = mock(JPAQuery.class);

        // Configure the query factory to return the mocked query
        when(queryFactory.select(Projections.constructor(
                CouponGetResponse.class,
                coupon.couponId,
                coupon.couponName,
                coupon.couponDescription,
                coupon.couponEffectivePeriod,
                coupon.couponEffectivePeriodUnit,
                coupon.couponCreateAt,
                coupon.couponExpiredAt,
                coupon.couponRule.id
        ))).thenReturn(mockedQuery);

        // Set the behavior for the mocked query
        when(mockedQuery.from(coupon)).thenReturn(mockedQuery);
        when(mockedQuery.orderBy(coupon.couponCreateAt.asc())).thenReturn(mockedQuery);
        when(mockedQuery.fetch()).thenReturn(mockCoupons);

        // Act
        List<CouponGetResponse> result = couponCustomRepository.getAllCoupons();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        CouponGetResponse coupon1 = result.get(0);
        assertEquals("Coupon 1", coupon1.getCouponName());
        assertEquals(10, coupon1.getCouponEffectivePeriod());
        assertEquals(CouponEffectivePeriodUnit.DAY, coupon1.getCouponEffectivePeriodUnit());

        CouponGetResponse coupon2 = result.get(1);
        assertEquals("Coupon 2", coupon2.getCouponName());
        assertEquals(5, coupon2.getCouponEffectivePeriod());
        assertEquals(CouponEffectivePeriodUnit.MONTH, coupon2.getCouponEffectivePeriodUnit());

        // QueryFactory 및 메서드 호출 검증
        verify(queryFactory, times(1)).select(any(Expression.class));
        verify(mockedQuery, times(1)).from(coupon);
        verify(mockedQuery, times(1)).fetch();
    }
}
