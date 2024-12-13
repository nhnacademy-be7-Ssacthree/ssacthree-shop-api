package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.controller;

import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto.MemberCouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.service.MemberCouponService;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop/members/coupons")
@RequiredArgsConstructor
public class MemberCouponController {

    private final MemberCouponService memberCouponService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<MemberCouponGetResponse>> getMemberCoupons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "couponIssueDate") String sort,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestHeader(name = "X-USER-ID") String memberLoginId) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);

        Sort sortOption = Sort.by(sortDirection, sort);

        Pageable pageable = PageRequest.of(page, size, sortOption);

        Long customerId = memberService.getCustomerIdByMemberLoginId(memberLoginId);

        Page<MemberCouponGetResponse> memberCouponGetResponsePage = memberCouponService.getMemberCoupons(customerId, pageable);

        return ResponseEntity.ok(memberCouponGetResponsePage);
    }
}
