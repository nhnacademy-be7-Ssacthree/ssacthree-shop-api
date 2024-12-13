package com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.orderset.deliveryrule.domain.DeliveryRule;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class OrderToStatusMappingTest {

  private Order mockOrder;
  private OrderStatus mockOrderStatus;

  @BeforeEach
  void setUp() {
    // Create mock objects for Order and OrderStatus
    mockOrder = new Order(
        1L,
        mock(Customer.class),
        null,
        LocalDateTime.now(),
        10000,
        "ORD12345",
        mock(DeliveryRule.class),
        "John Doe",
        "010-1234-5678",
        "12345",
        "Road Address",
        "Detail Address",
        "No request",
        null,
        "Invoice123"
    );

    mockOrderStatus = mock(OrderStatus.class); // Mock OrderStatus
  }

  @Test
  void testOrderToStatusMapping() {
    // Create an OrderToStatusMapping object
    LocalDateTime statusTime = LocalDateTime.now();
    OrderToStatusMapping orderToStatusMapping = new OrderToStatusMapping(
        mockOrder,
        mockOrderStatus,
        statusTime
    );

    // Verify the OrderToStatusMapping was created correctly
    assertEquals(mockOrder, orderToStatusMapping.getOrder());
    assertEquals(mockOrderStatus, orderToStatusMapping.getOrderStatus());
    assertEquals(statusTime, orderToStatusMapping.getOrderStatusCreatedAt());
  }

  @Test
  void testOrderToStatusMappingId() {
    // Create an OrderToStatusMappingId
    OrderToStatusMappingId mappingId = new OrderToStatusMappingId(mockOrder.getId(), mockOrderStatus.getId());

    // Verify the OrderToStatusMappingId fields
    assertEquals(mockOrder.getId(), mappingId.getOrder_id());
    assertEquals(mockOrderStatus.getId(), mappingId.getOrder_status_id());
  }
}

