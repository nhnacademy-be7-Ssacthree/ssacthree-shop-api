package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderListResponseTest {

  @Test
  void testOrderListResponseConstructor() {
    // Given
    Long orderId = 1L;
    LocalDate orderDate = LocalDate.of(2024, 12, 9);
    int totalPrice = 10000;
    String orderStatus = "PENDING";  // Test with "PENDING" status

    // Create OrderListResponse object using constructor
    OrderListResponse response = new OrderListResponse(orderId, orderDate, totalPrice, orderStatus);

    // Verify that the fields are correctly set
    assertEquals(orderId, response.getOrderId());
    assertEquals(orderDate, response.getOrderDate());
    assertEquals(totalPrice, response.getTotalPrice());
    assertEquals("대기", response.getOrderStatus());  // Verifying the converted order status

  }

  @Test
  void testConvertOrderStatusWithInvalidStatus() {
    // Given an invalid order status
    String invalidStatus = "UNKNOWN_STATUS";

    // Verify that the IllegalArgumentException is thrown
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      // Create an OrderListResponse with the invalid status
      new OrderListResponse(1L, LocalDate.now(), 1000, invalidStatus);
    });

    // Verify the exception message
    assertEquals("주문상태 변환오류: UNKNOWN_STATUS", exception.getMessage());
  }

  @Test
  void testOrderStatusConversion() {
    // Given
    String[] statuses = {"PENDING", "IN_SHOPPING", "COMPLETED", "RETURNED", "CANCELED"};
    String[] expectedConversions = {"대기", "배송중", "완료", "반품", "주문취소"};

    // Test that each status is correctly converted
    for (int i = 0; i < statuses.length; i++) {
      OrderListResponse response = new OrderListResponse(1L, LocalDate.now(), 1000, statuses[i]);
      assertEquals(expectedConversions[i], response.getOrderStatus());
    }
  }
}

