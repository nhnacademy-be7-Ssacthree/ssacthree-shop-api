package com.nhnacademy.ssacthree_shop_api.bookset.author.dto;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorNameResponseTest {

  @Test
  void testNoArgsConstructor() {
    // Given
    AuthorNameResponse response = new AuthorNameResponse();

    // When & Then
    assertThat(response).isNotNull(); // 객체가 생성되었는지 확인
    assertThat(response.getAuthorId()).isNull(); // authorId는 null로 초기화되어야 함
    assertThat(response.getAuthorName()).isNull(); // authorName은 null로 초기화되어야 함
  }

  @Test
  void testAllArgsConstructor() {
    // Given
    Long authorId = 1L;
    String authorName = "John Doe";

    // When
    AuthorNameResponse response = new AuthorNameResponse(authorId, authorName);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getAuthorId()).isEqualTo(authorId); // authorId 값 확인
    assertThat(response.getAuthorName()).isEqualTo(authorName); // authorName 값 확인
  }

  @Test
  void testConstructorFromAuthor() {
    // Given
    Author author = new Author(1L, "John Doe", "Author bio");

    // When
    AuthorNameResponse response = new AuthorNameResponse(author);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getAuthorId()).isEqualTo(author.getAuthorId()); // authorId 값 확인
    assertThat(response.getAuthorName()).isEqualTo(author.getAuthorName()); // authorName 값 확인
  }

  @Test
  void testConstructorWithAuthorName() {
    // Given
    String authorName = "John Doe";

    // When
    AuthorNameResponse response = new AuthorNameResponse(authorName);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getAuthorName()).isEqualTo(authorName); // authorName 값 확인
    assertThat(response.getAuthorId()).isNull(); // authorId는 null로 초기화되어야 함
  }

  @Test
  void testGetterMethods() {
    // Given
    Long authorId = 1L;
    String authorName = "John Doe";
    AuthorNameResponse response = new AuthorNameResponse(authorId, authorName);

    // When & Then
    assertThat(response.getAuthorId()).isEqualTo(authorId); // getAuthorId() 값 확인
    assertThat(response.getAuthorName()).isEqualTo(authorName); // getAuthorName() 값 확인
  }

  @Test
  void testToStringMethod() {
    // Given
    Long authorId = 1L;
    String authorName = "John Doe";
    AuthorNameResponse response = new AuthorNameResponse(authorId, authorName);

    // When
    String toStringResult = response.toString();

    // Then
    assertThat(toStringResult).contains("authorId=" + authorId);
    assertThat(toStringResult).contains("authorName=" + authorName);
  }

  @Test
  void testEqualsAndHashCode() {
    // Given
    AuthorNameResponse response1 = new AuthorNameResponse(1L, "John Doe");
    AuthorNameResponse response2 = new AuthorNameResponse(1L, "John Doe");

    // When & Then
    assertThat(response1).isEqualTo(response2); // 두 객체가 동일한지 확인
    assertThat(response1.hashCode()).isEqualTo(response2.hashCode()); // 두 객체의 hashCode가 동일한지 확인
  }
}
