package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookCategoryDtoTest {

  @Test
  void testAllArgsConstructorAndGetters() {
    // Mock 데이터 생성
    CategoryNameResponse categoryNameResponse = new CategoryNameResponse(1L, "Fiction");

    // 바로 AllArgsConstructor를 사용하여 객체 생성
    BookCategoryDto bookCategoryDto = new BookCategoryDto(1L, categoryNameResponse);

    // 값 검증
    assertEquals(1L, bookCategoryDto.getBookId());
    assertNotNull(bookCategoryDto.getCategoryNameResponse());
    assertEquals("Fiction", bookCategoryDto.getCategoryNameResponse().getCategoryName());
  }

  @Test
  void testConstructorWithNullValues() {
    // null 값을 가진 객체 생성
    BookCategoryDto bookCategoryDto = new BookCategoryDto(null, null);

    // null 값 검증
    assertNull(bookCategoryDto.getBookId());
    assertNull(bookCategoryDto.getCategoryNameResponse());
  }
}
