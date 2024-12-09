package com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackagingGetResponseTest {

  @Test
  void testPackagingGetResponse() {
    // Given
    Long expectedId = 1L;
    String expectedPackagingName = "Box";
    int expectedPackagingPrice = 500;
    String expectedPackagingImageUrl = "http://example.com/image.jpg";

    // Create PackagingGetResponse object using constructor
    PackagingGetResponse packagingGetResponse = new PackagingGetResponse(
        expectedId,
        expectedPackagingName,
        expectedPackagingPrice,
        expectedPackagingImageUrl
    );

    // When
    Long actualId = packagingGetResponse.getId();
    String actualPackagingName = packagingGetResponse.getPackagingName();
    int actualPackagingPrice = packagingGetResponse.getPackagingPrice();
    String actualPackagingImageUrl = packagingGetResponse.getPackagingImageUrl();

    // Then
    assertEquals(expectedId, actualId);
    assertEquals(expectedPackagingName, actualPackagingName);
    assertEquals(expectedPackagingPrice, actualPackagingPrice);
    assertEquals(expectedPackagingImageUrl, actualPackagingImageUrl);
  }
}
