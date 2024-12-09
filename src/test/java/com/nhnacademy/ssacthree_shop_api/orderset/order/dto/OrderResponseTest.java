package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderResponseTest {

  @Test
  void testOrderResponseConstructor() {
    // Given
    Long orderId = 123L;

    // When
    OrderResponse orderResponse = new OrderResponse(orderId);

    // Then
    assertNotNull(orderResponse);  // Verify that the object is not null
    assertEquals(orderId, orderResponse.getOrderId());  // Verify that the orderId is correctly set
  }
}

