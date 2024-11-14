package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CouponRuleUpdateRequest {

    @NotNull
    private Long couponRuleId;
}
