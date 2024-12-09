package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDtoTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    // 기본 생성자를 통해 객체 생성
    AuthorDto authorDto = new AuthorDto();

    // 세터로 값 설정
    authorDto.setAuthorId(1L);
    authorDto.setAuthorName("Author Name");
    authorDto.setAuthorInfo("Author Info");

    // 값 검증
    assertEquals(1L, authorDto.getAuthorId());
    assertEquals("Author Name", authorDto.getAuthorName());
    assertEquals("Author Info", authorDto.getAuthorInfo());
  }

  @Test
  void testAllArgsConstructorAndGetters() {
    // 모든 매개변수를 가진 생성자를 사용하여 객체 생성
    AuthorDto authorDto = new AuthorDto(1L, "Author Name", "Author Info");

    // 값 검증
    assertEquals(1L, authorDto.getAuthorId());
    assertEquals("Author Name", authorDto.getAuthorName());
    assertEquals("Author Info", authorDto.getAuthorInfo());
  }
}

