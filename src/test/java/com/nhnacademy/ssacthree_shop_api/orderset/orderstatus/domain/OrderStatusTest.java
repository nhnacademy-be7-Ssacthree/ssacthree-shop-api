package com.nhnacademy.ssacthree_shop_api.orderset.orderstatus.domain;

import com.nhnacademy.ssacthree_shop_api.orderset.ordertostatusmapping.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderStatusTest {

  private OrderStatus orderStatus;

  @BeforeEach
  void setUp() {
    // 기본 상태를 PENDING으로 초기화
    orderStatus = new OrderStatus(null, OrderStatusEnum.PENDING);
  }

  @Test
  void testConstructorAndGetter() {
    // 생성자와 getter 테스트
    assertNotNull(orderStatus);
    assertEquals(OrderStatusEnum.PENDING, orderStatus.getOrderStatusEnum());
  }

  @Test
  void testOrderStatusEnum() {
    // 다른 상태로 상태를 변경하여 테스트
    OrderStatus newOrderStatus = new OrderStatus(null, OrderStatusEnum.COMPLETED);
    assertEquals(OrderStatusEnum.COMPLETED, newOrderStatus.getOrderStatusEnum());
  }

  @Test
  void testDefaultOrderStatusEnum() {
    // 기본값을 테스트 (기본적으로 PENDING)
    OrderStatus defaultOrderStatus = new OrderStatus();
    assertEquals(OrderStatusEnum.PENDING, defaultOrderStatus.getOrderStatusEnum());
  }
}

