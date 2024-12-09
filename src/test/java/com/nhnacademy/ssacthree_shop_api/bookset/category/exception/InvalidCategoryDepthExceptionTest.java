package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCategoryDepthExceptionTest {

  @Test
  void testInvalidCategoryDepthException() {
    // Given
    int depth = 5;
    int maxDepth = 3;

    // Expected error message based on the constructor
    String expectedMessage = "5는 유효 깊이가 아닙니다. 유효 깊이는 1 또는 3로 지정 가능합니다.";

    // Create the InvalidCategoryDepthException instance with depth and maxDepth
    InvalidCategoryDepthException exception = new InvalidCategoryDepthException(depth, maxDepth);

    // Verify the exception message
    assertEquals(expectedMessage, exception.getMessage());

    // Verify the status code is set to 400 (Bad Request)
    assertEquals(400, exception.getCode());
  }
}

