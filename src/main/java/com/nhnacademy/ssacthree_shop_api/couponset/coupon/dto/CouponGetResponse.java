package com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.CouponEffectivePeriodUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CouponGetResponse {

    private Long couponId;
    private String couponName;
    private String couponDescription;
    private int couponEffectivePeriod;
    private CouponEffectivePeriodUnit couponEffectivePeriodUnit;
    private LocalDateTime couponCreateAt;
    private LocalDateTime couponExpiredAt;
    private Long couponRuleId;
}
