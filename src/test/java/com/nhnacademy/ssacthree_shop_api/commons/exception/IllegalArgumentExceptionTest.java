package com.nhnacademy.ssacthree_shop_api.commons.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalArgumentExceptionTest {

  @Test
  void testIllegalArgumentException() {
    // Given
    String expectedMessage = "Invalid argument provided";

    // Create the IllegalArgumentException instance with the message
    IllegalArgumentException exception = new IllegalArgumentException(expectedMessage);

    // Verify the message is set correctly
    assertEquals(expectedMessage, exception.getMessage());

    // Verify the status code is set correctly (400 for Bad Request)
    assertEquals(400, exception.getCode());
  }
}

