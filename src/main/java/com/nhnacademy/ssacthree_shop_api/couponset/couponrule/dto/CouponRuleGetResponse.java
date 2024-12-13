package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CouponRuleGetResponse {

    private Long couponRuleId;
    private CouponType couponType;
    private int couponMinOrderPrice;
    private int maxDiscountPrice;
    private int couponDiscountPrice;
    private String couponRuleName;
    private boolean couponIsUsed;
    private LocalDateTime couponRuleCreatedAt;
}
