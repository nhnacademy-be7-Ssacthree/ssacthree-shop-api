package com.nhnacademy.ssacthree_shop_api.couponset.coupon.service;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponUpdateRequest;

import java.util.List;

public interface CouponService {
    List<CouponGetResponse> getAllCoupons();

    Coupon createCoupon(CouponCreateRequest couponCreateRequest);

    Coupon updateCoupon(CouponUpdateRequest couponUpdateRequest);
}
