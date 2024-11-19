package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;

import java.util.List;

public interface CouponRuleService {
    List<CouponRuleGetResponse> getAllCouponRules();

    List<CouponRuleGetResponse> getAllSelectedCouponRules();

    CouponRule createCouponRule(CouponRuleCreateRequest couponRuleCreateRequest);

    CouponRule updateCouponRule(CouponRuleUpdateRequest couponRuleUpdateRequest);
}
