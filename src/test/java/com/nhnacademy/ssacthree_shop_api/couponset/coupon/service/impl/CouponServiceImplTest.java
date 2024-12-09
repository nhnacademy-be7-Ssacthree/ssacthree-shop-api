package com.nhnacademy.ssacthree_shop_api.couponset.coupon.service.impl;

import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.CouponEffectivePeriodUnit;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponCreateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponGetResponse;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.repository.CouponRepository;
import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponRule;
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
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponRuleRepository couponRuleRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon coupon;
    private CouponCreateRequest couponCreateRequest;
    private CouponUpdateRequest couponUpdateRequest;

    @Mock
    private CouponRule couponRule;

    @BeforeEach
    void setUp() {
        coupon = new Coupon("Coupon 1", "Description for Coupon 1", 10,
                CouponEffectivePeriodUnit.DAY, LocalDateTime.now().plusDays(10), couponRule);

        couponCreateRequest = new CouponCreateRequest();
        couponCreateRequest.setCouponName("Coupon 1");
        couponCreateRequest.setCouponDescription("Description for Coupon 1");
        couponCreateRequest.setCouponEffectivePeriod(10);
        couponCreateRequest.setCouponEffectivePeriodUnit(CouponEffectivePeriodUnit.DAY);
        couponCreateRequest.setCouponExpiredAt(LocalDateTime.now().plusDays(10));
        couponCreateRequest.setCouponRuleId(1L);

        couponUpdateRequest = new CouponUpdateRequest();
        couponUpdateRequest.setCouponId(1L);
    }

    @Test
    void testUpdateCoupon_InvalidCouponId() {
        // Arrange
        couponUpdateRequest.setCouponId(-1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> couponService.updateCoupon(couponUpdateRequest));
    }

    @Test
    void testGetAllCoupons() {
        // Arrange
        CouponGetResponse couponResponse = new CouponGetResponse(
                1L, "Coupon 1", "Description for Coupon 1", 10,
                CouponEffectivePeriodUnit.DAY, LocalDateTime.now(), LocalDateTime.now().plusDays(10), 1L);

        when(couponRepository.getAllCoupons()).thenReturn(Arrays.asList(couponResponse));

        // Act
        var result = couponService.getAllCoupons();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Coupon 1", result.get(0).getCouponName());
    }

    @Test
    void testCreateCoupon() {
        // Arrange
        when(couponRuleRepository.findById(couponCreateRequest.getCouponRuleId()))
                .thenReturn(Optional.of(couponRule));

        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // Act
        Coupon createdCoupon = couponService.createCoupon(couponCreateRequest);

        // Assert
        assertNotNull(createdCoupon);
        assertEquals("Coupon 1", createdCoupon.getCouponName());
        assertEquals(10, createdCoupon.getCouponEffectivePeriod());
        assertEquals(CouponEffectivePeriodUnit.DAY, createdCoupon.getCouponEffectivePeriodUnit());
    }

    @Test
    void testCreateCoupon_InvalidCouponRule() {
        // Arrange
        when(couponRuleRepository.findById(couponCreateRequest.getCouponRuleId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> couponService.createCoupon(couponCreateRequest));
    }

    @Test
    void testUpdateCoupon() {
        // Arrange: No need for mocking couponRepository.findById() because it's not used in the current implementation
        // Just call the updateCoupon method, which returns null for now

        // Act: Calling updateCoupon (not yet implemented, it will return null as a placeholder)
        Coupon updatedCoupon = couponService.updateCoupon(couponUpdateRequest);

        // Assert: Expecting the result to be null (since updateCoupon is not yet implemented)
        assertNull(updatedCoupon);
    }
}
