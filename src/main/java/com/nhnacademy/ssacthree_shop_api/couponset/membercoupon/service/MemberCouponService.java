package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponUpdateRequest;

import java.util.List;

public interface MemberCouponService {

    List<MemberCouponGetResponse> getAllMemberCoupons(Long customerId);

    MemberCoupon createMemberCoupon(MemberCouponCreateRequest memberCouponCreateRequest);

    MemberCoupon updateMemberCoupon(MemberCouponUpdateRequest memberCouponUpdateRequest);
}
