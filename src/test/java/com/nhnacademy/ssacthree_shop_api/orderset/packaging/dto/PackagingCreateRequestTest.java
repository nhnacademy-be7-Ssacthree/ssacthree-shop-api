package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackagingCreateRequestTest {

  @Test
  void testPackagingCreateRequest() {
    // Given
    String expectedName = "Box";
    int expectedPrice = 200;
    String expectedImageUrl = "http://example.com/image.jpg";

    // Create PackagingCreateRequest object and set values
    PackagingCreateRequest packagingCreateRequest = new PackagingCreateRequest();
    packagingCreateRequest.setName(expectedName);
    packagingCreateRequest.setPrice(expectedPrice);
    packagingCreateRequest.setImageUrl(expectedImageUrl);

    // When
    String actualName = packagingCreateRequest.getName();
    int actualPrice = packagingCreateRequest.getPrice();
    String actualImageUrl = packagingCreateRequest.getImageUrl();

    // Then
    assertEquals(expectedName, actualName);
    assertEquals(expectedPrice, actualPrice);
    assertEquals(expectedImageUrl, actualImageUrl);
  }
}

