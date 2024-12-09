package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorNotSetExceptionTest {

  @Test
  void testExceptionMessage() {
    // 예외 생성
    AuthorNotSetException exception = new AuthorNotSetException();

    // 메시지 검증
    assertEquals("작가가 설정되지 않았습니다. 작가를 설정해주세요.", exception.getMessage());
  }

  @Test
  void testExceptionThrown() {
    // 예외가 제대로 던져지는지 확인
    Exception exception = assertThrows(AuthorNotSetException.class, () -> {
      throw new AuthorNotSetException();
    });

    // 메시지 검증
    assertEquals("작가가 설정되지 않았습니다. 작가를 설정해주세요.", exception.getMessage());
  }
}
