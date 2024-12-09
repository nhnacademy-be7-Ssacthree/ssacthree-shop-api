package com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTagIdTest {

  private BookTagId bookTagId;

  @BeforeEach
  void setUp() {
    // BookTagId 객체 생성
    bookTagId = new BookTagId(1L, 2L);
  }

  @Test
  void testBookTagIdCreation() {
    // BookTagId 객체가 제대로 생성되었는지 확인
    assertNotNull(bookTagId);
    assertEquals(1L, bookTagId.getBookId());
    assertEquals(2L, bookTagId.getTagId());
  }

  @Test
  void testGetterMethods() {
    // Getter 메서드를 통해 bookId와 tagId 값 확인
    assertEquals(1L, bookTagId.getBookId());
    assertEquals(2L, bookTagId.getTagId());
  }


  @Test
  void testNoArgsConstructor() {
    // 기본 생성자 사용하여 객체 생성 후 값이 null이어야 함
    BookTagId defaultBookTagId = new BookTagId();
    assertNull(defaultBookTagId.getBookId());
    assertNull(defaultBookTagId.getTagId());
  }
}

