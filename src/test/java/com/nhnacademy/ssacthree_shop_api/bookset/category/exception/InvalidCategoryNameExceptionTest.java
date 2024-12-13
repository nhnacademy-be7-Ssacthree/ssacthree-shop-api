package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCategoryNameExceptionTest {

  @Test
  void testInvalidCategoryNameException() {
    // Given
    String expectedMessage = "Category name is invalid";

    // Create the InvalidCategoryNameException instance with the message
    InvalidCategoryNameException exception = new InvalidCategoryNameException(expectedMessage);

    // Verify the message is set correctly
    assertEquals(expectedMessage, exception.getMessage());

    // Verify the status code is set to 400 (Bad Request)
    assertEquals(400, exception.getCode());
  }
}
