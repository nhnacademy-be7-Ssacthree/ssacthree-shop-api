package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;

import java.util.List;

public interface CouponRuleCustomRepository {
    List<CouponRuleGetResponse> getAllCouponRules();

    List<CouponRuleGetResponse> getAllSelectedCouponRules();
}
