package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookAuthorDtoTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    // 기본 생성자를 통해 객체 생성
    BookAuthorDto bookAuthorDto;

    // Mock 데이터 생성
    AuthorNameResponse authorNameResponse = new AuthorNameResponse(1L, "John Doe");

    // 세터 없이 필드 초기화 (AllArgsConstructor 사용)
    bookAuthorDto = new BookAuthorDto(10L, authorNameResponse);

    // 값 검증
    assertEquals(10L, bookAuthorDto.getBookId());
    assertNotNull(bookAuthorDto.getAuthorNameResponse());
    assertEquals("John Doe", bookAuthorDto.getAuthorNameResponse().getAuthorName());
  }

  @Test
  void testAllArgsConstructorAndGetters() {
    // Mock 데이터 생성
    AuthorNameResponse authorNameResponse = new AuthorNameResponse(1L, "Jane Smith");

    // 모든 매개변수를 가진 생성자로 객체 생성
    BookAuthorDto bookAuthorDto = new BookAuthorDto(20L, authorNameResponse);

    // 값 검증
    assertEquals(20L, bookAuthorDto.getBookId());
    assertEquals("Jane Smith", bookAuthorDto.getAuthorNameResponse().getAuthorName());
    assertEquals(1L, bookAuthorDto.getAuthorNameResponse().getAuthorId());
  }

  @Test
  void testNullValues() {
    // null 값을 가진 객체 생성
    BookAuthorDto bookAuthorDto = new BookAuthorDto(null, null);

    // null 값 검증
    assertNull(bookAuthorDto.getBookId());
    assertNull(bookAuthorDto.getAuthorNameResponse());
  }
}
