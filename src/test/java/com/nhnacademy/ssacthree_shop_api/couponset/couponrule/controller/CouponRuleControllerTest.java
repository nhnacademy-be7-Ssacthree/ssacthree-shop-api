package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.config.SecurityConfig;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.CouponRuleService;
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

@WebMvcTest(CouponRuleController.class)
@Import(SecurityConfig.class)
class CouponRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponRuleService couponRuleService;

    @Test
    @DisplayName("모든 쿠폰 룰 조회 테스트")
    void getAllCouponRules() throws Exception {
        // Arrange
        CouponRuleGetResponse response = new CouponRuleGetResponse(
                1L,
                CouponType.RATIO,
                10000,
                5000,
                1000,
                "할인 룰",
                true,
                LocalDateTime.now()
        );
        when(couponRuleService.getAllCouponRules()).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/shop/admin/coupon-rules")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].couponRuleId").value(1L))
                .andExpect(jsonPath("$[0].couponRuleName").value("할인 룰"));
    }

    @Test
    @DisplayName("선택된 쿠폰 룰 조회 테스트")
    void getAllSelectedCouponRules() throws Exception {
        // Arrange
        CouponRuleGetResponse response = new CouponRuleGetResponse(
                1L,
                CouponType.CASH,
                10000,
                5000,
                5000,
                "현금 룰",
                true,
                LocalDateTime.now()
        );
        when(couponRuleService.getAllSelectedCouponRules()).thenReturn(List.of(response));

        // Act & Assert
        mockMvc.perform(get("/api/shop/admin/coupon-rules/selected")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].couponRuleId").value(1L))
                .andExpect(jsonPath("$[0].couponRuleName").value("현금 룰"));
    }

    @Test
    @DisplayName("쿠폰 룰 수정 테스트")
    void updateCouponRule() throws Exception {
        // Arrange
        CouponRuleUpdateRequest request = new CouponRuleUpdateRequest();
        request.setCouponRuleId(1L);

        // Act & Assert
        mockMvc.perform(put("/api/shop/admin/coupon-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));
    }

    @Test
    @DisplayName("쿠폰 룰 생성 테스트")
    void createCouponRule() throws Exception {
        // Arrange
        CouponRuleCreateRequest request = new CouponRuleCreateRequest();
        request.setCouponRuleName("할인 쿠폰 규칙");
        request.setCouponType(CouponType.RATIO);
        request.setCouponMinOrderPrice(10000);
        request.setMaxDiscountPrice(1000);
        request.setCouponDiscountPrice(10); // 10% 할인 (RATIO 타입)

        // Act & Assert
        mockMvc.perform(post("/api/shop/admin/coupon-rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())  // 201 Created 응답
                .andExpect(jsonPath("$.message").value("생성 성공"));
    }
}
