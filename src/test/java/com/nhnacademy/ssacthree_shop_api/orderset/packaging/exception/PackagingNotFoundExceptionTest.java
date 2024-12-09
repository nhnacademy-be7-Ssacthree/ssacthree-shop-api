package com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PackagingNotFoundExceptionTest {

  @Test
  void testPackagingNotFoundExceptionWithMessage() {
    // Given
    String expectedMessage = "Packaging not found for the given ID";

    // When
    PackagingNotFoundException exception = assertThrows(PackagingNotFoundException.class, () -> {
      throw new PackagingNotFoundException(expectedMessage);
    });

    // Then
    assertEquals(expectedMessage, exception.getMessage());
  }
}

