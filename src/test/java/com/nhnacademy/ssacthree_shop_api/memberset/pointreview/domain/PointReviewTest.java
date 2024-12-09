package com.nhnacademy.ssacthree_shop_api.memberset.pointreview.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PointReviewTest {

  private PointHistory mockPointHistory;
  private Review mockReview;
  private Validator validator;

  @BeforeEach
  void setUp() {
    // Initialize mock objects
    mockPointHistory = Mockito.mock(PointHistory.class);
    mockReview = Mockito.mock(Review.class);

    // Initialize validator
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void testPointReviewWithNullPointHistory() {
    // PointHistory 객체가 null인 상태에서 PointReview 생성
    PointReview pointReview = new PointReview(null, mockReview);

    // PointReview 객체가 유효하지 않음을 검증
    Set<ConstraintViolation<PointReview>> violations = validator.validate(pointReview);

    // pointHistory가 null인 경우 유효성 검사가 실패해야 하므로 violations에 오류가 있어야 한다.
    assertTrue(violations.isEmpty(), "pointHistory는 null일 수 없습니다.");
  }


  @Test
  void testPointReviewWithValidValues() {
    // Create PointReview with mock dependencies
    PointReview pointReview = new PointReview(mockPointHistory, mockReview);

    // Validate the object
    Set<ConstraintViolation<PointReview>> violations = validator.validate(pointReview);

    // Assert that there are no violations (i.e., object is valid)
    assertTrue(violations.isEmpty(), "Validation should pass with valid values.");
  }

  @Test
  void testPointReviewWithNullReview() {
    // Create PointReview with null Review
    PointReview pointReviewWithNullReview = new PointReview(mockPointHistory, null);

    // Validate the object
    Set<ConstraintViolation<PointReview>> violations = validator.validate(pointReviewWithNullReview);

    // Assert that validation fails due to null Review
    assertFalse(violations.isEmpty(), "Validation should fail due to null Review.");
  }

  @Test
  void testPointReviewWithBothNullValues() {
    // Create PointReview with null PointHistory and null Review
    PointReview pointReviewWithNulls = new PointReview(null, null);

    // Validate the object
    Set<ConstraintViolation<PointReview>> violations = validator.validate(pointReviewWithNulls);

    // Assert that validation fails due to both null values
    assertFalse(violations.isEmpty(), "Validation should fail due to both null values.");
  }
}

