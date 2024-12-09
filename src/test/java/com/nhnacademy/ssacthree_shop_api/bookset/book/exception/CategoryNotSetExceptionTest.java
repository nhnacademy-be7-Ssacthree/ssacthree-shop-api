package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryNotSetExceptionTest {

  @Test
  void testCategoryNotSetException() {
    // Create the exception instance
    CategoryNotSetException exception = new CategoryNotSetException();

    // Verify the exception message
    assertEquals("카테고리가 설정되지 않았습니다. 카테고리를 1개 이상 10개 이하로 설정하시오.", exception.getMessage());

    // Verify that the status code is 400 by calling the getCode() method (assuming CustomException has this method)
    assertEquals(400, exception.getCode());  // Verifies the status code set in the exception
  }
}
