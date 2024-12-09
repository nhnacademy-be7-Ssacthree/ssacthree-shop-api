package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangeOrderStatusRequestTest {

  @Test
  void testChangeOrderStatusRequestConstructorAndGetters() {
    // Given
    Long orderId = 123L;
    String status = "SHIPPED";

    // Create an instance using the all-args constructor
    ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(orderId, status);

    // Verify that the orderId is set correctly
    assertEquals(orderId, request.getOrderId());

    // Verify that the status is set correctly
    assertEquals(status, request.getStatus());
  }
}
