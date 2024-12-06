package com.nhnacademy.ssacthree_shop_api.couponset.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.CouponEffectivePeriodUnit;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
@Import(SecurityConfig.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @Test
    @DisplayName("모든 쿠폰 조회 테스트")
    void getAllCoupons() throws Exception {
        // Arrange
        CouponGetResponse response = new CouponGetResponse(
                1L,
                "할인 쿠폰",
                "설명",
                30,
                CouponEffectivePeriodUnit.DAY,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                1L
        );
        when(couponService.getAllCoupons()).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/shop/admin/coupons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].couponId").value(1L))
                .andExpect(jsonPath("$[0].couponName").value("할인 쿠폰"));
    }

    @Test
    @DisplayName("쿠폰 수정 테스트")
    void updateCoupon() throws Exception {
        // Arrange
        CouponUpdateRequest request = new CouponUpdateRequest();
        request.setCouponId(1L);

        Coupon updatedCoupon = new Coupon(
                "업데이트된 쿠폰 이름",
                "업데이트된 설명",
                30,
                CouponEffectivePeriodUnit.DAY,
                LocalDateTime.now().plusDays(30),
                null // 실제 쿠폰 룰 설정 필요
        );
        when(couponService.updateCoupon(Mockito.any(CouponUpdateRequest.class))).thenReturn(updatedCoupon);

        // Act & Assert
        mockMvc.perform(put("/api/shop/admin/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));
    }

    @Test
    @DisplayName("쿠폰 생성 테스트")
    void createCoupon() throws Exception {
        // Arrange
        CouponCreateRequest request = new CouponCreateRequest();
        request.setCouponName("할인 쿠폰");
        request.setCouponDescription("설명");
        request.setCouponEffectivePeriod(30);
        request.setCouponEffectivePeriodUnit(CouponEffectivePeriodUnit.DAY);
        request.setCouponExpiredAt(LocalDateTime.now().plusDays(30));
        request.setCouponRuleId(1L);

        Coupon createdCoupon = new Coupon(
                "할인 쿠폰",
                "설명",
                30,
                CouponEffectivePeriodUnit.DAY,
                LocalDateTime.now().plusDays(30),
                null // 실제 쿠폰 룰 설정 필요
        );
        when(couponService.createCoupon(Mockito.any(CouponCreateRequest.class))).thenReturn(createdCoupon);

        // Act & Assert
        mockMvc.perform(post("/api/shop/admin/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("생성 성공"));
    }
}
