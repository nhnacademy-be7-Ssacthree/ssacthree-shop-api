package com.nhnacademy.ssacthree_shop_api.couponset.coupon.service.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.QCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.service.CouponService;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.CouponRuleRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CouponRepository couponRepository;
    private final CouponRuleRepository couponRuleRepository;

    @Override
    public List<CouponGetResponse> getAllCoupons() {
        QCoupon coupon = QCoupon.coupon;

        return new JPAQueryFactory(entityManager)
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

    @Override
    public Coupon createCoupon(CouponCreateRequest couponCreateRequest) {
        CouponRule couponRule = couponRuleRepository.findById(couponCreateRequest.getCouponRuleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 규칙입니다."));

        Coupon coupon = new Coupon(
                couponCreateRequest.getCouponName(),
                couponCreateRequest.getCouponDescription(),
                couponCreateRequest.getCouponEffectivePeriod(),
                couponCreateRequest.getCouponEffectivePeriodUnit(),
                couponCreateRequest.getCouponExpiredAt(),
                couponRule
        );

        return couponRepository.save(coupon);
    }

    @Override
    public Coupon updateCoupon(CouponUpdateRequest couponUpdateRequest) {

        Long couponId = couponUpdateRequest.getCouponId();
        if (couponId <= 0) {
            throw new IllegalArgumentException("존재하지 않는 쿠폰입니다.");
        }


        // 어떻게 변경할지 고민

        return null;
    }
}
