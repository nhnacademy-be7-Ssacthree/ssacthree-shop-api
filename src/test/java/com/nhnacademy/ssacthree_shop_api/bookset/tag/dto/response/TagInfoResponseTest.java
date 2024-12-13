package com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagInfoResponseTest {

  @Test
  void testNoArgsConstructor() {
    // Given
    TagInfoResponse response = new TagInfoResponse();

    // When & Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getTagId()).isNull(); // getTagId()가 null인지 확인
    assertThat(response.getTagName()).isNull(); // getTagName()이 null인지 확인
  }

  @Test
  void testAllArgsConstructor() {
    // Given
    Long tagId = 1L;
    String tagName = "Fiction";

    // When
    TagInfoResponse response = new TagInfoResponse(tagId, tagName);

    // Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getTagId()).isEqualTo(tagId); // getTagId()가 예상한 tagId와 일치하는지 확인
    assertThat(response.getTagName()).isEqualTo(tagName); // getTagName()이 예상한 tagName과 일치하는지 확인
  }

  @Test
  void testConstructorFromTag() {
    // Given
    Tag tag = new Tag(1L, "Fiction");

    // When
    TagInfoResponse response = new TagInfoResponse(tag);

    // Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getTagId()).isEqualTo(tag.getTagId()); // getTagId()가 Tag 객체의 tagId와 일치하는지 확인
    assertThat(response.getTagName()).isEqualTo(tag.getTagName()); // getTagName()이 Tag 객체의 tagName과 일치하는지 확인
  }

  @Test
  void testConstructorWithTagName() {
    // Given
    String tagName = "Fiction";

    // When
    TagInfoResponse response = new TagInfoResponse(tagName);

    // Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getTagName()).isEqualTo(tagName); // getTagName()이 tagName과 일치하는지 확인
    assertThat(response.getTagId()).isNull(); // getTagId()는 null이어야 함
  }

  @Test
  void testGetterMethods() {
    // Given
    Long tagId = 1L;
    String tagName = "Fiction";
    TagInfoResponse response = new TagInfoResponse(tagId, tagName);

    // When & Then
    assertThat(response.getTagId()).isEqualTo(tagId); // getTagId()가 예상한 tagId와 일치하는지 확인
    assertThat(response.getTagName()).isEqualTo(tagName); // getTagName()이 예상한 tagName과 일치하는지 확인
  }

  @Test
  void testToStringMethod() {
    // Given
    Long tagId = 1L;
    String tagName = "Fiction";
    TagInfoResponse response = new TagInfoResponse(tagId, tagName);

    // When
    String toStringResult = response.toString();

    // Then
    assertThat(toStringResult)
        .contains("tagId=" + tagId) // tagId가 toString() 결과에 포함되는지 확인
        .contains("tagName=" + tagName); // tagName이 toString() 결과에 포함되는지 확인
  }

  @Test
  void testEqualsAndHashCode() {
    // Given
    TagInfoResponse response1 = new TagInfoResponse(1L, "Fiction");
    TagInfoResponse response2 = new TagInfoResponse(1L, "Fiction");

    // When & Then
    assertThat(response1).isEqualTo(response2); // response1과 response2가 동일한지 확인
    assertThat(response1).hasSameHashCodeAs(response2); // response1과 response2가 동일한 hashCode를 가지는지 확인
  }
}
