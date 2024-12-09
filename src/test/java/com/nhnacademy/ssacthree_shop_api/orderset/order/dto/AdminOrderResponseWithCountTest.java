package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminOrderResponseWithCountTest {

  @Test
  void testNoArgsConstructorAndSetters() {
    // 기본 생성자를 통해 객체 생성
    AdminOrderResponseWithCount response = new AdminOrderResponseWithCount();

    // Mock 데이터 생성
    List<AdminOrderListResponse> orders = new ArrayList<>();
    orders.add(new AdminOrderListResponse(
        1L,
        LocalDate.of(2024, 12, 1),
        50000,
        "Delivered",
        LocalDate.of(2024, 12, 2),
        "John Doe",
        "ORD12345",
        "INV67890"
    ));
    orders.add(new AdminOrderListResponse(
        2L,
        LocalDate.of(2024, 12, 3),
        75000,
        "Pending",
        LocalDate.of(2024, 12, 3),
        "Jane Smith",
        "ORD54321",
        "INV09876"
    ));

    // 세터로 값 설정
    response.setOrders(orders);
    response.setTotalOrders(2L);

    // 값 검증
    assertEquals(2, response.getOrders().size());
    assertEquals(2L, response.getTotalOrders());

    AdminOrderListResponse firstOrder = response.getOrders().get(0);
    assertEquals(1L, firstOrder.getOrderId());
    assertEquals(LocalDate.of(2024, 12, 1), firstOrder.getOrderDate());
    assertEquals(50000, firstOrder.getTotalPrice());
    assertEquals("Delivered", firstOrder.getOrderStatus());
    assertEquals(LocalDate.of(2024, 12, 2), firstOrder.getOrderStatusCreatedAt());
    assertEquals("John Doe", firstOrder.getCustomerName());
    assertEquals("ORD12345", firstOrder.getOrderNumber());
    assertEquals("INV67890", firstOrder.getInvoiceNumber());
  }

  @Test
  void testAllArgsConstructorAndGetters() {
    // Mock 데이터 생성
    List<AdminOrderListResponse> orders = new ArrayList<>();
    orders.add(new AdminOrderListResponse(
        1L,
        LocalDate.of(2024, 12, 1),
        50000,
        "Delivered",
        LocalDate.of(2024, 12, 2),
        "John Doe",
        "ORD12345",
        "INV67890"
    ));

    // 모든 매개변수를 가진 생성자를 통해 객체 생성
    AdminOrderResponseWithCount response = new AdminOrderResponseWithCount(orders, 1L);

    // 값 검증
    assertEquals(1, response.getOrders().size());
    assertEquals(1L, response.getTotalOrders());

    AdminOrderListResponse firstOrder = response.getOrders().get(0);
    assertEquals(1L, firstOrder.getOrderId());
    assertEquals(LocalDate.of(2024, 12, 1), firstOrder.getOrderDate());
    assertEquals(50000, firstOrder.getTotalPrice());
    assertEquals("Delivered", firstOrder.getOrderStatus());
    assertEquals(LocalDate.of(2024, 12, 2), firstOrder.getOrderStatusCreatedAt());
    assertEquals("John Doe", firstOrder.getCustomerName());
    assertEquals("ORD12345", firstOrder.getOrderNumber());
    assertEquals("INV67890", firstOrder.getInvoiceNumber());
  }

  @Test
  void testEmptyOrders() {
    // 빈 리스트를 가진 객체 생성
    AdminOrderResponseWithCount response = new AdminOrderResponseWithCount(new ArrayList<>(), 0L);

    // 값 검증
    assertTrue(response.getOrders().isEmpty());
    assertEquals(0L, response.getTotalOrders());
  }
}

