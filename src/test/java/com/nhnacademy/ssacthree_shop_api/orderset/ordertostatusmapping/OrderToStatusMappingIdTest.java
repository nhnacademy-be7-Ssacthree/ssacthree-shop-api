package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderToStatusMappingIdTest {

  @Test
  void testEqualsAndHashCode() {
    // Create two OrderToStatusMappingId objects with the same values
    OrderToStatusMappingId id1 = new OrderToStatusMappingId(1L, 2L);
    OrderToStatusMappingId id2 = new OrderToStatusMappingId(1L, 2L);

    // Create a different OrderToStatusMappingId object with different values
    OrderToStatusMappingId id3 = new OrderToStatusMappingId(1L, 3L);

    // Assert that id1 and id2 are equal
    assertEquals(id1, id2);

    // Assert that id1 and id3 are not equal
    assertNotEquals(id1, id3);

    // Assert that the hashCode of id1 and id2 are the same
    assertEquals(id1.hashCode(), id2.hashCode());

    // Assert that the hashCode of id1 and id3 are different
    assertNotEquals(id1.hashCode(), id3.hashCode());
  }

  @Test
  void testConstructor() {
    // Create a new OrderToStatusMappingId with specific values
    OrderToStatusMappingId id = new OrderToStatusMappingId(1L, 2L);

    // Assert that the order_id and order_status_id are correctly set
    assertEquals(1L, id.getOrder_id());
    assertEquals(2L, id.getOrder_status_id());
  }

  @Test
  void testDefaultConstructor() {
    // Create a new OrderToStatusMappingId with the default constructor
    OrderToStatusMappingId id = new OrderToStatusMappingId();

    // Assert that the order_id and order_status_id are null by default
    assertNull(id.getOrder_id());
    assertNull(id.getOrder_status_id());
  }
}

