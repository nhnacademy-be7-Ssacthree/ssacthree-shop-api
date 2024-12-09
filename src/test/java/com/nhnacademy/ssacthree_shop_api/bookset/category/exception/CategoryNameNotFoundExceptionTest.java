package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNameNotFoundExceptionTest {

  @Test
  void testCategoryNameNotFoundException() {
    String categoryName = "Fiction";

    // Create the exception instance
    CategoryNameNotFoundException exception = new CategoryNameNotFoundException(categoryName);

    // Verify the exception message
    assertEquals("\"Fiction\"과 일치하는 카테고리가 없습니다.", exception.getMessage());

    // Manually verify the status code (since we know it's set as 404 in the constructor)
    assertEquals(404, exception.getCode());  // Verify that the status code passed is 404
  }
}
