package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthorNameResponseTest {

  private Author mockAuthor;

  @BeforeEach
  void setUp() {
    // Author 객체를 생성하고 초기화
    mockAuthor = new Author(1L, "J.K. Rowling", "Info"); // Author 클래스의 생성자에 맞게 초기화
  }

  @Test
  void testNoArgsConstructor() {
    // @NoArgsConstructor로 기본 생성자를 테스트
    AuthorNameResponse authorNameResponse = new AuthorNameResponse();

    // 기본 생성자로 생성된 객체의 authorId와 authorName은 null이어야 한다.
    assertNull(authorNameResponse.getAuthorId());
    assertNull(authorNameResponse.getAuthorName());
  }

  @Test
  void testAllArgsConstructor() {
    // 모든 인자를 받는 생성자를 테스트
    AuthorNameResponse authorNameResponse = new AuthorNameResponse(1L, "J.K. Rowling");

    // authorId와 authorName이 정상적으로 초기화되었는지 확인
    assertEquals(1L, authorNameResponse.getAuthorId());
    assertEquals("J.K. Rowling", authorNameResponse.getAuthorName());
  }

  @Test
  void testConstructorWithAuthor() {
    // Author 객체를 받는 생성자를 테스트
    AuthorNameResponse authorNameResponse = new AuthorNameResponse(mockAuthor);

    // Author 객체에서 authorId와 authorName을 정상적으로 받아왔는지 확인
    assertEquals(mockAuthor.getAuthorId(), authorNameResponse.getAuthorId());
    assertEquals(mockAuthor.getAuthorName(), authorNameResponse.getAuthorName());
  }

  @Test
  void testConstructorWithAuthorName() {
    // AuthorName만 받는 생성자를 테스트
    AuthorNameResponse authorNameResponse = new AuthorNameResponse("J.K. Rowling");

    // authorName만 설정되었는지 확인
    assertNull(authorNameResponse.getAuthorId()); // authorId는 null이어야 한다.
    assertEquals("J.K. Rowling", authorNameResponse.getAuthorName());
  }

  @Test
  void testSetterAndGetter() {
    // setter와 getter가 제대로 동작하는지 테스트
    AuthorNameResponse authorNameResponse = new AuthorNameResponse();

    // setter를 사용하여 값 설정
    authorNameResponse.setAuthorId(2L);
    authorNameResponse.setAuthorName("George Orwell");

    // getter를 사용하여 값 확인
    assertEquals(2L, authorNameResponse.getAuthorId());
    assertEquals("George Orwell", authorNameResponse.getAuthorName());
  }
}

