package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class PaymentNotFoundExceptionTest {

  @Test
  void testConstructor() {
    // Given
    String expectedMessage = "Payment not found";

    // When
    PaymentNotFoundException exception = new PaymentNotFoundException(expectedMessage);

    // Then
    assertNotNull(exception);
    assertEquals(expectedMessage, exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
  }

  @Test
  void testInheritance() {
    // Given
    PaymentNotFoundException exception = new PaymentNotFoundException("Payment not found");

    // Then
    assertTrue(exception instanceof OrderDetailCustomException, "The exception should be an instance of OrderDetailCustomException");
  }

  @Test
  void testStatus() {
    // Given
    PaymentNotFoundException exception = new PaymentNotFoundException("Payment not found");

    // Then
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus(), "The HTTP status should be 404 NOT FOUND");
  }
}

