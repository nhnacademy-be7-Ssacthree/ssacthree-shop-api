package com.nhnacademy.ssacthree_shop_api.memberset.pointrefund.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.orderset.refund.domain.Refund;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PointRefundTest {

  private PointHistory mockPointHistory;
  private Refund mockRefund;
  private PointRefund pointRefund;
  private Validator validator;

  @BeforeEach
  void setUp() {
    // Mock the dependencies
    mockPointHistory = Mockito.mock(PointHistory.class);
    mockRefund = Mockito.mock(Refund.class);

    // Create the PointRefund object using the mock objects
    pointRefund = new PointRefund(mockPointHistory, mockRefund);
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void testConstructorAndGetters() {
    // Given
    // Mocked objects are already initialized in setUp() method

    // When
    PointHistory pointHistory = pointRefund.getPointHistory();
    Refund refund = pointRefund.getRefund();

    // Then
    assertNotNull(pointHistory);
    assertNotNull(refund);
    assertEquals(mockPointHistory, pointHistory);
    assertEquals(mockRefund, refund);
  }

  @Test
  void testPointRefundWithNullValues() {
    // Rename the local variable to avoid conflict
    PointRefund pointRefundWithNullValues = new PointRefund(null, null);

    // Validate the object
    Set<ConstraintViolation<PointRefund>> violations = validator.validate(pointRefundWithNullValues);

    // Assert that validation fails (i.e., violations are not empty)
    Assertions.assertFalse(violations.isEmpty(), "Validation should fail due to null values.");
  }

  @Test
  void testPointRefundWithValidValues() {
    // Test constructor with valid (non-null) values
    // No exceptions expected

    PointRefund validPointRefund = new PointRefund(mockPointHistory, mockRefund);

    assertNotNull(validPointRefund);
    assertEquals(mockPointHistory, validPointRefund.getPointHistory());
    assertEquals(mockRefund, validPointRefund.getRefund());
  }
}
