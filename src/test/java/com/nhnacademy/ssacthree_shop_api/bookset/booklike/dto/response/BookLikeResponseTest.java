package com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookLikeResponseTest {

  @Test
  void testNoArgsConstructor() {
    // Given
    BookLikeResponse response = new BookLikeResponse();

    // When & Then
    assertThat(response).isNotNull(); // 객체가 생성되었는지 확인
    assertThat(response.getBookId()).isNull(); // bookId가 null로 초기화되는지 확인
    assertThat(response.getLikeCount()).isNull(); // likeCount가 null로 초기화되는지 확인
  }

  @Test
  void testBookLikeResponseConstructorWithBookIdAndLikeCount() {
    // Given
    Long bookId = 1L;
    Long likeCount = 10L;

    // When
    BookLikeResponse response = new BookLikeResponse(bookId, likeCount);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getBookId()).isEqualTo(bookId); // bookId 값 확인
    assertThat(response.getLikeCount()).isEqualTo(likeCount); // likeCount 값 확인
  }

  @Test
  void testBookLikeResponseConstructorWithBookLikeRequestAndLikeCount() {
    // Given
    BookLikeRequest request;
    // BookLikeRequest에서 bookId 값을 설정하는 방법은 생성자만 가능
    request = new BookLikeRequest();
    Long likeCount = 10L;

    // When
    BookLikeResponse response = new BookLikeResponse(request, likeCount);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getBookId()).isEqualTo(request.getBookId()); // bookId 값 확인
    assertThat(response.getLikeCount()).isEqualTo(likeCount); // likeCount 값 확인
  }

  @Test
  void testBookLikeRequestDefaultValues() {
    // Given
    BookLikeRequest request = new BookLikeRequest();

    // When & Then
    assertThat(request).isNotNull(); // 객체가 생성되었는지 확인
    assertThat(request.getBookId()).isNull(); // bookId가 null인지 확인
  }
}
