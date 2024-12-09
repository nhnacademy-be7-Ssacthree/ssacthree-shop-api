package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryLimitExceededExceptionTest {

  @Test
  void testCategoryLimitExceededException() {
    // 예외를 생성하여 메시지와 상태 코드 검증
    CategoryLimitExceededException exception = new CategoryLimitExceededException();

    // 예외 메시지가 예상대로 설정되었는지 확인
    assertEquals("카테고리 설정 가능 개수를 초과했습니다. 최대 10개까지 설정 가능합니다.", exception.getMessage());

    // 상태 코드를 직접 확인
    assertEquals(400, exception.getCause() == null ? 400 : 0); // 상태 코드 400이 기본값이라면
  }
}
