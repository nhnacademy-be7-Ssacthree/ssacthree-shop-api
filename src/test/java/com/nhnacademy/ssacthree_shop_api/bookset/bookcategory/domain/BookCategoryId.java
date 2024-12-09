package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookCategoryIdTest {

  @Test
  void testAllArgsConstructorAndGetters() {
    // Mock 데이터 생성
    Long bookId = 1L;
    Long categoryId = 2L;

    // 모든 매개변수를 가진 생성자로 객체 생성
    BookCategoryId bookCategoryId = new BookCategoryId(bookId, categoryId);

    // 값 검증
    assertEquals(bookId, bookCategoryId.getBookId());
    assertEquals(categoryId, bookCategoryId.getCategoryId());
  }

  @Test
  void testNoArgsConstructor() {
    // 기본 생성자로 객체 생성
    BookCategoryId bookCategoryId = new BookCategoryId();

    // 기본 값 검증 (null)
    assertNull(bookCategoryId.getBookId());
    assertNull(bookCategoryId.getCategoryId());
  }

}
