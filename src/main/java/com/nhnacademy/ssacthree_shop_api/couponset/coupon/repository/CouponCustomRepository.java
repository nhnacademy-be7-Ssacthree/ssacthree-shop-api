package com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;

import java.util.List;

public interface CouponCustomRepository {
    List<CouponGetResponse> getAllCoupons();
}
