package com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PackagingTest {

  @Test
  void testAllArgsConstructor() {
    // Arrange
    String name = "Standard Box";
    int price = 500;
    String imageUrl = "http://example.com/image.jpg";

    // Act
    Packaging packaging = new Packaging(name, price, imageUrl);

    // Assert
    assertNotNull(packaging, "Packaging object should not be null");
    assertEquals(name, packaging.getPackagingName(), "Packaging name should be initialized correctly");
    assertEquals(price, packaging.getPackagingPrice(), "Packaging price should be initialized correctly");
    assertEquals(imageUrl, packaging.getPackagingImageUrl(), "Packaging image URL should be initialized correctly");
  }

  @Test
  void testSetters() {
    // Arrange
    Packaging packaging = new Packaging("Initial Name", 100, "http://initial.com/image.jpg");

    // Act
    packaging.setPackagingName("Updated Name");
    packaging.setPackagingPrice(200);
    packaging.setPackagingImageUrl("http://updated.com/image.jpg");

    // Assert
    assertEquals("Updated Name", packaging.getPackagingName(), "Packaging name should be updated");
    assertEquals(200, packaging.getPackagingPrice(), "Packaging price should be updated");
    assertEquals("http://updated.com/image.jpg", packaging.getPackagingImageUrl(), "Packaging image URL should be updated");
  }

  @Test
  void testNoArgsConstructor() {
    // Act
    Packaging packaging = new Packaging();

    // Assert
    assertNotNull(packaging, "No-args constructor should create a non-null object");
    assertNull(packaging.getPackagingName(), "Packaging name should be null by default");
    assertEquals(0, packaging.getPackagingPrice(), "Packaging price should be 0 by default");
    assertNull(packaging.getPackagingImageUrl(), "Packaging image URL should be null by default");
  }
}

