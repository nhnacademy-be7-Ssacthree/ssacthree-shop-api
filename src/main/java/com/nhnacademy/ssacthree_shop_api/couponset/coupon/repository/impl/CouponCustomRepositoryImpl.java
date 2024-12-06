package com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.QCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.CouponCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponCustomRepositoryImpl implements CouponCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CouponGetResponse> getAllCoupons() {
        QCoupon coupon = QCoupon.coupon;

        return queryFactory
                .select(Projections.constructor(
                        CouponGetResponse.class,
                        coupon.couponId,
                        coupon.couponName,
                        coupon.couponDescription,
                        coupon.couponEffectivePeriod,
                        coupon.couponEffectivePeriodUnit,
                        coupon.couponCreateAt,
                        coupon.couponExpiredAt,
                        coupon.couponRule.id
                ))
                .from(coupon)
                .orderBy(coupon.couponCreateAt.asc())
                .fetch();
    }
}
