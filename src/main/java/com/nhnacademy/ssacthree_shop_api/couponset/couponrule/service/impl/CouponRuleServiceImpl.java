package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.CouponRuleRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.CouponRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponRuleServiceImpl implements CouponRuleService {

    private final CouponRuleRepository couponRuleRepository;

    @Override
    public List<CouponRuleGetResponse> getAllCouponRules() {
        return couponRuleRepository.getAllCouponRules();
    }

    @Override
    public List<CouponRuleGetResponse> getAllSelectedCouponRules() {
        return couponRuleRepository.getAllSelectedCouponRules();
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
