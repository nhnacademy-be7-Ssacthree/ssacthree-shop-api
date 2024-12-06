package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.service.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.repository.CouponRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponRuleServiceImplTest {

    @Mock
    private CouponRuleRepository couponRuleRepository;

    @InjectMocks
    private CouponRuleServiceImpl couponRuleService;

    private CouponRule couponRule;
    private CouponRuleCreateRequest couponRuleCreateRequest;
    private CouponRuleUpdateRequest couponRuleUpdateRequest;

    @BeforeEach
    void setUp() {
        couponRule = new CouponRule(
                CouponType.CASH, 1000, 500, 500, "Cash Coupon"
        );

        couponRuleCreateRequest = new CouponRuleCreateRequest();
        couponRuleCreateRequest.setCouponRuleName("Cash Coupon");
        couponRuleCreateRequest.setCouponType(CouponType.CASH);
        couponRuleCreateRequest.setCouponMinOrderPrice(1000);
        couponRuleCreateRequest.setMaxDiscountPrice(500);
        couponRuleCreateRequest.setCouponDiscountPrice(500);

        couponRuleUpdateRequest = new CouponRuleUpdateRequest();
        couponRuleUpdateRequest.setCouponRuleId(1L);
    }

    @Test
    void testGetAllCouponRules() {
        // Arrange
        CouponRuleGetResponse couponRuleResponse = new CouponRuleGetResponse(
                1L, CouponType.CASH, 1000, 500, 500, "Cash Coupon", true, LocalDateTime.now()
        );

        when(couponRuleRepository.getAllCouponRules()).thenReturn(Arrays.asList(couponRuleResponse));

        // Act
        var result = couponRuleService.getAllCouponRules();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cash Coupon", result.get(0).getCouponRuleName());
    }

    @Test
    void testGetAllSelectedCouponRules() {
        // Arrange
        CouponRuleGetResponse couponRuleResponse = new CouponRuleGetResponse(
                1L, CouponType.CASH, 1000, 500, 500, "Cash Coupon", true, LocalDateTime.now()
        );

        when(couponRuleRepository.getAllSelectedCouponRules()).thenReturn(Arrays.asList(couponRuleResponse));

        // Act
        var result = couponRuleService.getAllSelectedCouponRules();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cash Coupon", result.get(0).getCouponRuleName());
    }

    @Test
    void testCreateCouponRule() {
        // Arrange
        when(couponRuleRepository.save(any(CouponRule.class))).thenReturn(couponRule);

        // Act
        CouponRule createdCouponRule = couponRuleService.createCouponRule(couponRuleCreateRequest);

        // Assert
        assertNotNull(createdCouponRule);
        assertEquals("Cash Coupon", createdCouponRule.getCouponRuleName());
        assertEquals(CouponType.CASH, createdCouponRule.getCouponType());
        assertEquals(1000, createdCouponRule.getCouponMinOrderPrice());
        assertEquals(500, createdCouponRule.getMaxDiscountPrice());
    }

    @Test
    void testUpdateCouponRule_InvalidCouponRuleId() {
        // Arrange
        couponRuleUpdateRequest.setCouponRuleId(-1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> couponRuleService.updateCouponRule(couponRuleUpdateRequest));
    }

    @Test
    void testUpdateCouponRule_CouponRuleNotFound() {
        // Arrange
        when(couponRuleRepository.findById(couponRuleUpdateRequest.getCouponRuleId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> couponRuleService.updateCouponRule(couponRuleUpdateRequest));
    }

    @Test
    void testUpdateCouponRule_SuccessfulUpdate() {
        // Arrange
        couponRule.setCouponIsUsed(true); // 초기값을 true로 설정
        when(couponRuleRepository.findById(couponRuleUpdateRequest.getCouponRuleId()))
                .thenReturn(Optional.of(couponRule)); // 쿠폰 정책이 존재하는 경우
        when(couponRuleRepository.save(any(CouponRule.class))).thenReturn(couponRule); // save를 mock하여 저장된 couponRule 반환

        // Act
        CouponRule updatedCouponRule = couponRuleService.updateCouponRule(couponRuleUpdateRequest);

        // Assert
        assertNotNull(updatedCouponRule);
        assertEquals(false, updatedCouponRule.isCouponIsUsed()); // 쿠폰 사용 여부가 반전되었는지 확인
    }
}
