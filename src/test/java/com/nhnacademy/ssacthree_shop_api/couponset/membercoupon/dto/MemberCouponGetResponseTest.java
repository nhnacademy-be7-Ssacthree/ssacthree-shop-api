package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MemberCouponGetResponseTest {

  @Test
  void testAllArgsConstructorAndGetters() {
    // Arrange
    String couponName = "Discount Coupon";
    String couponDescription = "10% off on your next purchase";
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime expiredAt = createdAt.plusDays(30);
    LocalDateTime usedAt = createdAt.plusDays(5);

    // Act
    MemberCouponGetResponse response = new MemberCouponGetResponse(
        couponName, couponDescription, createdAt, expiredAt, usedAt
    );

    // Assert
    assertEquals(couponName, response.getCouponName());
    assertEquals(couponDescription, response.getCouponDescription());
    assertEquals(createdAt, response.getMemberCouponCreatedAt());
    assertEquals(expiredAt, response.getMemberCouponExpiredAt());
    assertEquals(usedAt, response.getMemberCouponUsedAt());
  }

  @Test
  void testNoArgsConstructor() {
    // Act
    MemberCouponGetResponse response = new MemberCouponGetResponse();

    // Assert
    assertNull(response.getCouponName());
    assertNull(response.getCouponDescription());
    assertNull(response.getMemberCouponCreatedAt());
    assertNull(response.getMemberCouponExpiredAt());
    assertNull(response.getMemberCouponUsedAt());
  }
}
