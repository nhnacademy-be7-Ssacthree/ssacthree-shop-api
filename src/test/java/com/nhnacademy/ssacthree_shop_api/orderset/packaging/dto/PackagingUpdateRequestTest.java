package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PackagingUpdateRequestTest {

  @Test
  void testPackagingUpdateRequestConstructorAndGetters() {
    // Given
    String expectedName = "New Packaging";
    int expectedPrice = 2000;
    String expectedImageUrl = "http://example.com/image.jpg";

    // When
    PackagingUpdateRequest request = new PackagingUpdateRequest(expectedName, expectedPrice, expectedImageUrl);

    // Then
    assertEquals(expectedName, request.getPackagingName());
    assertEquals(expectedPrice, request.getPackagingPrice());
    assertEquals(expectedImageUrl, request.getPackagingImageUrl());
  }


  @Test
  void testPackagingUpdateRequestNoArgsConstructor() {
    // Given
    PackagingUpdateRequest request = new PackagingUpdateRequest();

    // Then
    // Ensure that all fields are null or set to default values (since no args constructor is used)
    assertNull(request.getPackagingName(), "Packaging name should be null");
    assertEquals(0, request.getPackagingPrice(), "Packaging price should be 0");
    assertNull(request.getPackagingImageUrl(), "Packaging image URL should be null");
  }

}

