package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MemberCouponGetResponse {

    private Long memberCouponId;
    private Long couponId;
    private LocalDateTime memberCouponCreatedAt;
    private LocalDateTime memberCouponExpiredAt;
    private LocalDateTime memberCouponUsedAt;
}
