package com.nhnacademy.ssacthree_shop_api.commons.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {

  @Test
  void testConflictException() {
    // Given
    String expectedMessage = "This is a conflict message";

    // Create the ConflictException instance with the message
    ConflictException exception = new ConflictException(expectedMessage);

    // Verify the message is set correctly
    assertEquals(expectedMessage, exception.getMessage());

    // Verify the status code is set correctly (409 for conflict)
    assertEquals(409, exception.getCode());
  }
}
