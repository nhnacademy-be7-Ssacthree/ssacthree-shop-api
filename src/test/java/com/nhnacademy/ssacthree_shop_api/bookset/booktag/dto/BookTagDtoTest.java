package com.nhnacademy.ssacthree_shop_api.bookset.booktag.dto;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookTagDtoTest {

  private TagInfoResponse tagInfoResponse;
  private BookTagDto bookTagDto;

  @BeforeEach
  void setUp() {
    // Tag 객체 모의(mock) 생성
    Tag tag = mock(Tag.class);
    when(tag.getTagId()).thenReturn(1L);
    when(tag.getTagName()).thenReturn("Science");

    // TagInfoResponse 객체 생성
    tagInfoResponse = new TagInfoResponse(tag);

    // BookTagDto 객체 생성
    bookTagDto = new BookTagDto(1L, tagInfoResponse);
  }

  @Test
  void testBookTagDtoCreation() {
    // BookTagDto 객체 생성 확인
    assertNotNull(bookTagDto);
    assertEquals(1L, bookTagDto.getBookId());
    assertEquals(tagInfoResponse, bookTagDto.getTagInfoResponse());
  }

  @Test
  void testTagInfoResponseCreationWithTag() {
    // TagInfoResponse 객체 생성 확인
    assertEquals(1L, tagInfoResponse.getTagId());
    assertEquals("Science", tagInfoResponse.getTagName());
  }

  @Test
  void testTagInfoResponseCreationWithTagName() {
    // TagInfoResponse 객체를 tagName만 가지고 생성
    TagInfoResponse tagInfoResponseWithName = new TagInfoResponse("Technology");

    assertNull(tagInfoResponseWithName.getTagId()); // TagId는 null이어야 함
    assertEquals("Technology", tagInfoResponseWithName.getTagName());
  }

  @Test
  void testBookIdInBookTagDto() {
    // bookId 검증
    assertEquals(1L, bookTagDto.getBookId());
  }

  @Test
  void testTagInfoResponseGetterMethods() {
    // Getter 메서드를 통해 값이 올바르게 설정되었는지 확인 (중복 제거)
    assertAll("Check getter methods for TagInfoResponse",
        () -> assertEquals(1L, tagInfoResponse.getTagId()),
        () -> assertEquals("Science", tagInfoResponse.getTagName())
    );
  }
}

