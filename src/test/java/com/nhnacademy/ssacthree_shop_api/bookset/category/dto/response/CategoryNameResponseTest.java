package com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response;


import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryNameResponseTest {

  @Test
  void testNoArgsConstructor() {
    // Given
    CategoryNameResponse response = new CategoryNameResponse();

    // When & Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getCategoryId()).isNull(); // getCategoryId()가 null인지 확인
    assertThat(response.getCategoryName()).isNull(); // getCategoryName()이 null인지 확인
  }

  @Test
  void testAllArgsConstructor() {
    // Given
    Long categoryId = 1L;
    String categoryName = "Fiction";

    // When
    CategoryNameResponse response = new CategoryNameResponse(categoryId, categoryName);

    // Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getCategoryId()).isEqualTo(categoryId); // getCategoryId()가 예상한 categoryId와 일치하는지 확인
    assertThat(response.getCategoryName()).isEqualTo(categoryName); // getCategoryName()이 예상한 categoryName과 일치하는지 확인
  }

  @Test
  void testConstructorFromCategory() {
    // Given
    Category category = new Category(1L, "Fiction", true, null, null);

    // When
    CategoryNameResponse response = new CategoryNameResponse(category);

    // Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getCategoryId()).isEqualTo(category.getCategoryId()); // getCategoryId()가 Category 객체의 categoryId와 일치하는지 확인
    assertThat(response.getCategoryName()).isEqualTo(category.getCategoryName()); // getCategoryName()이 Category 객체의 categoryName과 일치하는지 확인
  }

  @Test
  void testConstructorWithCategoryName() {
    // Given
    String categoryName = "Fiction";

    // When
    CategoryNameResponse response = new CategoryNameResponse(categoryName);

    // Then
    assertThat(response).isNotNull(); // 객체가 null이 아닌지 확인
    assertThat(response.getCategoryName()).isEqualTo(categoryName); // getCategoryName()이 categoryName과 일치하는지 확인
    assertThat(response.getCategoryId()).isNull(); // getCategoryId()는 null이어야 함
  }

  @Test
  void testGetterMethods() {
    // Given
    Long categoryId = 1L;
    String categoryName = "Fiction";
    CategoryNameResponse response = new CategoryNameResponse(categoryId, categoryName);

    // When & Then
    assertThat(response.getCategoryId()).isEqualTo(categoryId); // getCategoryId()가 예상한 categoryId와 일치하는지 확인
    assertThat(response.getCategoryName()).isEqualTo(categoryName); // getCategoryName()이 예상한 categoryName과 일치하는지 확인
  }

  @Test
  void testToStringMethod() {
    // Given
    Long categoryId = 1L;
    String categoryName = "Fiction";
    CategoryNameResponse response = new CategoryNameResponse(categoryId, categoryName);

    // When
    String toStringResult = response.toString();

    // Then
    assertThat(toStringResult)
        .contains("categoryId=" + categoryId) // categoryId가 toString() 결과에 포함되는지 확인
        .contains("categoryName=" + categoryName); // categoryName이 toString() 결과에 포함되는지 확인
  }

  @Test
  void testEqualsAndHashCode() {
    // Given
    CategoryNameResponse response1 = new CategoryNameResponse(1L, "Fiction");
    CategoryNameResponse response2 = new CategoryNameResponse(1L, "Fiction");

    // When & Then
    assertThat(response1).isEqualTo(response2); // response1과 response2가 동일한지 확인
    assertThat(response1).hasSameHashCodeAs(response2); // response1과 response2가 동일한 hashCode를 가지는지 확인
  }
}

