package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNotUsableExceptionTest {

  @Test
  void testCategoryNotUsableException() {
    Long categoryId = 123L;  // Sample category ID

    // Create the exception instance
    CategoryNotUsableException exception = new CategoryNotUsableException(categoryId);

    // Verify the exception message contains the category ID
    String expectedMessage = "현재 카테고리는 사용 중이지 않으므로 사용 또는 수정이 불가합니다. (카테고리 ID: " + categoryId + ")";
    assertEquals(expectedMessage, exception.getMessage());

    // Verify that the status code is 400 by calling the getCode() method (assuming CustomException has this method)
    assertEquals(400, exception.getCode());  // Verifies the status code set in the exception
  }
}
