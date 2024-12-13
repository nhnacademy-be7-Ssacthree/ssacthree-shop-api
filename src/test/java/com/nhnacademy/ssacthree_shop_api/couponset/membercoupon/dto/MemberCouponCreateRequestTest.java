package com.nhnacademy.ssacthree_shop_api.couponset.membercoupon.dto;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemberCouponCreateRequestTest {

  private final Validator validator;

  public MemberCouponCreateRequestTest() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testAllArgsConstructor() {
    // Arrange
    LocalDateTime expiredAt = LocalDateTime.of(2024, 12, 31, 23, 59);
    LocalDateTime usedAt = LocalDateTime.of(2024, 12, 1, 12, 0);
    Long couponId = 1L;
    Long customerId = 2L;

    // Act
    MemberCouponCreateRequest request = new MemberCouponCreateRequest(expiredAt, usedAt, couponId, customerId);

    // Assert
    assertEquals(expiredAt, request.getMemberCouponExpiredAt());
    assertEquals(usedAt, request.getMemberCouponUsedAt());
    assertEquals(couponId, request.getCouponId());
    assertEquals(customerId, request.getCustomerId());
  }

  @Test
  void testNoArgsConstructorAndSetters() {
    // Arrange
    MemberCouponCreateRequest request = new MemberCouponCreateRequest();
    LocalDateTime expiredAt = LocalDateTime.of(2024, 12, 31, 23, 59);
    LocalDateTime usedAt = LocalDateTime.of(2024, 12, 1, 12, 0);
    Long couponId = 1L;
    Long customerId = 2L;

    // Act
    request.setMemberCouponExpiredAt(expiredAt);
    request.setMemberCouponUsedAt(usedAt);
    request.setCouponId(couponId);
    request.setCustomerId(customerId);

    // Assert
    assertEquals(expiredAt, request.getMemberCouponExpiredAt());
    assertEquals(usedAt, request.getMemberCouponUsedAt());
    assertEquals(couponId, request.getCouponId());
    assertEquals(customerId, request.getCustomerId());
  }

  @Test
  void testValidationWithValidData() {
    // Arrange
    MemberCouponCreateRequest request = new MemberCouponCreateRequest(
        LocalDateTime.of(2024, 12, 31, 23, 59),
        LocalDateTime.of(2024, 12, 1, 12, 0),
        1L,
        2L
    );

    // Act
    Set<ConstraintViolation<MemberCouponCreateRequest>> violations = validator.validate(request);

    // Assert
    assertTrue(violations.isEmpty());
  }

  @Test
  void testValidationWithMissingRequiredFields() {
    // Arrange
    MemberCouponCreateRequest request = new MemberCouponCreateRequest();

    // Act
    Set<ConstraintViolation<MemberCouponCreateRequest>> violations = validator.validate(request);

    // Assert
    assertFalse(violations.isEmpty());
    assertEquals(3, violations.size()); // memberCouponExpiredAt, couponId, customerId
  }

  @Test
  void testValidationWithNullCouponId() {
    // Arrange
    MemberCouponCreateRequest request = new MemberCouponCreateRequest(
        LocalDateTime.of(2024, 12, 31, 23, 59),
        LocalDateTime.of(2024, 12, 1, 12, 0),
        null,
        2L
    );

    // Act
    Set<ConstraintViolation<MemberCouponCreateRequest>> violations = validator.validate(request);

    // Assert
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("couponId")));
  }

  @Test
  void testValidationWithNullCustomerId() {
    // Arrange
    MemberCouponCreateRequest request = new MemberCouponCreateRequest(
        LocalDateTime.of(2024, 12, 31, 23, 59),
        LocalDateTime.of(2024, 12, 1, 12, 0),
        1L,
        null
    );

    // Act
    Set<ConstraintViolation<MemberCouponCreateRequest>> violations = validator.validate(request);

    // Assert
    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("customerId")));
  }
}
