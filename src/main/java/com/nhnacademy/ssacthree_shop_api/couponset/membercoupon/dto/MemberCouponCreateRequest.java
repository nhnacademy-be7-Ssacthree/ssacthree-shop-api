package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class MemberCouponCreateRequest {

    @NotNull
    private LocalDateTime memberCouponExpiredAt;

    private LocalDateTime memberCouponUsedAt;

    @NotNull
    private Long couponId;

    @NotNull
    private Long customerId;
}
