package com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PackagingAlreadyExistsExceptionTest {

  @Test
  void testExceptionMessage() {
    // Given a specific message for the exception
    String message = "Packaging already exists!";

    // When the exception is thrown
    PackagingAlreadyExistsException exception = assertThrows(PackagingAlreadyExistsException.class, () -> {
      throw new PackagingAlreadyExistsException(message);
    });

    // Then verify the message
    assertEquals(message, exception.getMessage());
  }
}

