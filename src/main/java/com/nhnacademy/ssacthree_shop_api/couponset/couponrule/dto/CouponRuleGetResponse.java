package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
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
