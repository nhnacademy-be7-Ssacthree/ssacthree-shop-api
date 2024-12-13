package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDetailIdTest {

  @Test
  void testEqualsAndHashCode() {
    // Arrange
    OrderDetailId id1 = new OrderDetailId(1L, 100L);
    OrderDetailId id2 = new OrderDetailId(1L, 100L);
    OrderDetailId id3 = new OrderDetailId(2L, 200L);

    // Assert equals
    assertEquals(id1, id2, "Objects with same values should be equal");
    assertNotEquals(id1, id3, "Objects with different values should not be equal");

    // Assert hashCode
    assertEquals(id1.hashCode(), id2.hashCode(), "Objects with same values should have same hashCode");
    assertNotEquals(id1.hashCode(), id3.hashCode(), "Objects with different values should have different hashCode");
  }

  @Test
  void testNoArgsConstructor() {
    // Act
    OrderDetailId id = new OrderDetailId();

    // Assert
    assertNotNull(id, "No-args constructor should create a non-null object");
  }

  @Test
  void testAllArgsConstructor() {
    // Act
    OrderDetailId id = new OrderDetailId(1L, 100L);

    // Assert
    assertNotNull(id, "All-args constructor should create a non-null object");
    assertEquals(1L, id.getOrder_id(), "Order ID should be initialized correctly");
    assertEquals(100L, id.getBook_id(), "Book ID should be initialized correctly");
  }

  @Test
  void testEqualsWithNull() {
    // Arrange
    OrderDetailId id = new OrderDetailId(1L, 100L);

    // Assert
    assertNotEquals(id, null, "Object should not be equal to null");
  }

  @Test
  void testEqualsWithDifferentClass() {
    // Arrange
    OrderDetailId id = new OrderDetailId(1L, 100L);

    // Assert
    assertNotEquals(id, "String", "Object should not be equal to an object of a different class");
  }
}

