package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.QCouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.CouponRuleRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.CouponRuleService;
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
public class CouponRuleServiceImpl implements CouponRuleService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CouponRuleRepository couponRuleRepository;

    @Override
    public List<CouponRuleGetResponse> getAllCouponRules() {
        QCouponRule couponRule = QCouponRule.couponRule;

        return new JPAQueryFactory(entityManager)
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

    @Transactional(readOnly = true)
    @Override
    public CouponRule getSelectedCouponRule() {
        return couponRuleRepository.findAll()
                .stream()
                .filter(CouponRule::isCouponIsUsed)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("선택된 쿠폰 정책이 없습니다."));
    }

    @Override
    public CouponRule createCouponRule(CouponRuleCreateRequest couponRuleCreateRequest) {
        CouponRule couponRule = new CouponRule(
                couponRuleCreateRequest.getCouponType(),
                couponRuleCreateRequest.getCouponMinOrderPrice(),
                couponRuleCreateRequest.getMaxDiscountPrice(),
                couponRuleCreateRequest.getCouponDiscountPrice(),
                couponRuleCreateRequest.getCouponRuleName()
        );

        return couponRuleRepository.save(couponRule);
    }

    @Override
    public CouponRule updateCouponRule(CouponRuleUpdateRequest couponRuleUpdateRequest) {

        Long couponRuleId = couponRuleUpdateRequest.getCouponRuleId();
        if (couponRuleId <= 0) {
            throw new IllegalArgumentException("쿠폰 정책 ID가 잘못되었습니다.");
        }

        CouponRule couponRule = couponRuleRepository.findById(couponRuleId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰 정책이 존재하지 않습니다."));

        couponRule.setCouponIsUsed(!couponRule.isCouponIsUsed());

        return couponRuleRepository.save(couponRule);
    }
}
