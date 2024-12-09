package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderResponseWithCountTest {

  @Test
  void testOrderResponseWithCountConstructor() {
    // Given
    OrderListResponse order1 = new OrderListResponse(1L, LocalDate.of(2024, 12, 1), 1000, "PENDING");
    OrderListResponse order2 = new OrderListResponse(2L, LocalDate.of(2024, 12, 2), 2000, "IN_SHOPPING");
    long totalOrders = 2;

    // When
    OrderResponseWithCount orderResponseWithCount = new OrderResponseWithCount(
        Arrays.asList(order1, order2), totalOrders);

    // Then
    assertNotNull(orderResponseWithCount);  // Verify the object is not null
    assertEquals(2, orderResponseWithCount.getOrders().size());  // Verify the size of the orders list
    assertEquals(totalOrders, orderResponseWithCount.getTotalOrders());  // Verify the totalOrders value

    // Verify specific order details
    assertEquals(1L, orderResponseWithCount.getOrders().get(0).getOrderId());  // First order ID
    assertEquals("대기", orderResponseWithCount.getOrders().get(0).getOrderStatus());  // First order status
    assertEquals(2L, orderResponseWithCount.getOrders().get(1).getOrderId());  // Second order ID
    assertEquals("배송중", orderResponseWithCount.getOrders().get(1).getOrderStatus());  // Second order status
  }
}
