package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberCouponUpdateRequest {

    @NotNull
    private Long memberCouponId;
}
