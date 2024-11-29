package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberCouponGetResponse {

    private String couponName;
    private String couponDescription;
    private LocalDateTime memberCouponCreatedAt;
    private LocalDateTime memberCouponExpiredAt;
    private LocalDateTime memberCouponUsedAt;
}
