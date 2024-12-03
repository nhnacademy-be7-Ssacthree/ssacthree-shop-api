package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.controller;

import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/members/coupons")
@RequiredArgsConstructor
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    @GetMapping("/not-used")
    public ResponseEntity<Page<MemberCouponGetResponse>> getNotUsedMemberCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "memberCouponCreatedAt:asc") String[] sort,
            @RequestHeader(name = "X-USER-ID") Long memberId) {

        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<MemberCouponGetResponse> coupons = memberCouponService.getNotUsedMemberCoupons(pageable, memberId);

        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @GetMapping("/used")
    public ResponseEntity<Page<MemberCouponGetResponse>> getUsedMemberCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "memberCouponCreatedAt:asc") String[] sort,
            @RequestHeader(name = "X-USER-ID") Long memberId) {

        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<MemberCouponGetResponse> coupons = memberCouponService.getUsedMemberCoupons(pageable, memberId);

        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }
}
