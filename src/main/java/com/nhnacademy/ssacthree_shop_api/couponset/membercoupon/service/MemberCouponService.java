package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.domain.MemberCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponService {

    MemberCoupon createMemberCoupon(MemberCouponCreateRequest memberCouponCreateRequest);

    MemberCoupon updateMemberCoupon(MemberCouponUpdateRequest memberCouponUpdateRequest);

    Page<MemberCouponGetResponse> getNotUsedMemberCoupons(Pageable pageable, Long memberId);

    Page<MemberCouponGetResponse> getUsedMemberCoupons(Pageable pageable, Long memberId);
}
