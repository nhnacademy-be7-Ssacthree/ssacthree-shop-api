package com.nhnacademy.ssacthree_shop_api.couponset.coupon.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/admin/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<List<CouponGetResponse>> getAllCoupons() {
        return ResponseEntity.ok().body(couponService.getAllCoupons());
    }

    @PutMapping
    ResponseEntity<MessageResponse> updateCoupon(
            @Valid @RequestBody CouponUpdateRequest couponUpdateRequest) {

        couponService.updateCoupon(couponUpdateRequest);
        MessageResponse messageResponse = new MessageResponse("수정 성공");

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping
    ResponseEntity<MessageResponse> createCoupon(
            @Valid @RequestBody CouponCreateRequest couponCreateRequest) {

        couponService.createCoupon(couponCreateRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}
