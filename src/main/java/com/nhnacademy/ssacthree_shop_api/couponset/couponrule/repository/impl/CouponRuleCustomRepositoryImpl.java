package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.QCouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.CouponRuleCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRuleCustomRepositoryImpl implements CouponRuleCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CouponRuleGetResponse> getAllCouponRules() {
        QCouponRule couponRule = QCouponRule.couponRule;

        return queryFactory
                .select(Projections.constructor(
                        CouponRuleGetResponse.class,
                        couponRule.id,
                        couponRule.couponType,
                        couponRule.couponMinOrderPrice,
                        couponRule.maxDiscountPrice,
                        couponRule.couponDiscountPrice,
                        couponRule.couponRuleName,
                        couponRule.couponIsUsed,
                        couponRule.couponRuleCreatedAt
                ))
                .from(couponRule)
                .orderBy(couponRule.couponIsUsed.desc())
                .orderBy(couponRule.couponRuleCreatedAt.asc())
                .fetch();
    }

    @Override
    public List<CouponRuleGetResponse> getAllSelectedCouponRules() {
        QCouponRule couponRule = QCouponRule.couponRule;

        return queryFactory
                .select(Projections.constructor(
                        CouponRuleGetResponse.class,
                        couponRule.id,
                        couponRule.couponType,
                        couponRule.couponMinOrderPrice,
                        couponRule.maxDiscountPrice,
                        couponRule.couponDiscountPrice,
                        couponRule.couponRuleName,
                        couponRule.couponIsUsed,
                        couponRule.couponRuleCreatedAt
                ))
                .from(couponRule)
                .where(couponRule.couponIsUsed.eq(true))
                .orderBy(couponRule.couponRuleCreatedAt.asc())
                .fetch();
    }
}
