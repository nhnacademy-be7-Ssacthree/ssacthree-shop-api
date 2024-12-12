package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemberCouponUpdateRequestTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testNoArgsConstructorAndSetters() {
    // Act
    MemberCouponUpdateRequest request = new MemberCouponUpdateRequest();
    request.setMemberCouponId(1L);

    // Assert
    assertNotNull(request.getMemberCouponId());
    assertEquals(1L, request.getMemberCouponId());
  }

  @Test
  void testNotNullValidationSuccess() {
    // Arrange
    MemberCouponUpdateRequest request = new MemberCouponUpdateRequest();
    request.setMemberCouponId(1L);

    // Act
    Set<ConstraintViolation<MemberCouponUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertTrue(violations.isEmpty());
  }

  @Test
  void testNotNullValidationFailure() {
    // Arrange
    MemberCouponUpdateRequest request = new MemberCouponUpdateRequest();

    // Act
    Set<ConstraintViolation<MemberCouponUpdateRequest>> violations = validator.validate(request);

    // Assert
    assertFalse(violations.isEmpty());
    assertEquals(1, violations.size());
    ConstraintViolation<MemberCouponUpdateRequest> violation = violations.iterator().next();
    assertEquals("memberCouponId", violation.getPropertyPath().toString());
    assertEquals("널이어서는 안됩니다", violation.getMessage());
  }
}
