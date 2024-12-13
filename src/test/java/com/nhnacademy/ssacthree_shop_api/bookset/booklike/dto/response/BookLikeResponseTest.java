package com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BookLikeRequestResponseTest {

  @Test
  void testBookLikeRequest() {
    // Given
    BookLikeRequest request;

    // When
    request = new BookLikeRequest();

    // Then
    assertThat(request).isNotNull();
    assertThat(request.getBookId()).isNull();
  }

  @Test
  void testBookLikeResponseWithBookIdAndLikeCount() {
    // Given
    Long bookId = 1L;
    Long likeCount = 10L;

    // When
    BookLikeResponse response = new BookLikeResponse(bookId, likeCount);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getBookId()).isEqualTo(bookId);
    assertThat(response.getLikeCount()).isEqualTo(likeCount);
  }

  @Test
  void testBookLikeResponseWithBookLikeRequest() {
    // Given
    Long bookId = 1L;
    Long likeCount = 5L;
    BookLikeRequest request = new BookLikeRequest();

    // Simulate bookId injection into the request
    java.lang.reflect.Field bookIdField;
    try {
      bookIdField = BookLikeRequest.class.getDeclaredField("bookId");
      bookIdField.setAccessible(true);
      bookIdField.set(request, bookId);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    // When
    BookLikeResponse response = new BookLikeResponse(request, likeCount);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getBookId()).isEqualTo(request.getBookId());
    assertThat(response.getLikeCount()).isEqualTo(likeCount);
  }

  @Test
  void testSetLikeCount() {
    // Given
    BookLikeResponse response = new BookLikeResponse();
    Long likeCount = 20L;

    // When
    response.setLikeCount(likeCount);

    // Then
    assertThat(response.getLikeCount()).isEqualTo(likeCount);
  }
}