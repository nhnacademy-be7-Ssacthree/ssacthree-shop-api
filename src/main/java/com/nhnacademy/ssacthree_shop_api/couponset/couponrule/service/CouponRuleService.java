package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;

import java.util.List;

public interface CouponRuleService {
    CouponRule createCouponRule(CouponRuleCreateRequest couponRuleCreateRequest);

    CouponRule getSelectedCouponRule();

    CouponRule updateCouponRule(CouponRuleUpdateRequest couponRuleUpdateRequest);

    List<CouponRuleGetResponse> getAllCouponRules();
}
