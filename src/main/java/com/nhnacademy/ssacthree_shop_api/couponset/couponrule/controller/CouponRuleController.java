package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.impl.CouponRuleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop/admin/coupon-rules")
@RequiredArgsConstructor
public class CouponRuleController {

    private final CouponRuleServiceImpl couponRuleService;

    @GetMapping
    public ResponseEntity<List<CouponRuleGetResponse>> getAllCouponRules() {
        return ResponseEntity.ok().body(couponRuleService.getAllCouponRules());
    }

    @PutMapping
    ResponseEntity<MessageResponse> updateCouponRule(
            @Valid @RequestBody CouponRuleUpdateRequest couponRuleUpdateRequest) {

        couponRuleService.updateCouponRule(couponRuleUpdateRequest);
        MessageResponse messageResponse = new MessageResponse("수정 성공");

        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping
    ResponseEntity<MessageResponse> createCouponRule(
            @Valid @RequestBody CouponRuleCreateRequest couponRuleCreateRequest) {

        couponRuleService.createCouponRule(couponRuleCreateRequest);
        MessageResponse messageResponse = new MessageResponse("생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}
