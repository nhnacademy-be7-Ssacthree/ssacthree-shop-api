package com.nhnacademy.ssacthree_shop_api.orderset.payment.dto;

import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestTest {

  @Test
  void testConstructorAndGetters() {
    // Given
    Long orderId = 1L;
    Long method = 1234L;
    Integer amount = 5000;
    String status = "APPROVED";
    String paymentKey = "paymentKey123";
    String approvedAt = "2024-12-09T15:30:00";

    // When
    PaymentRequest paymentRequest = new PaymentRequest(orderId, method, amount, status, paymentKey, approvedAt);

    // Then
    assertEquals(orderId, paymentRequest.getOrderId());
    assertEquals(method, paymentRequest.getMethod());
    assertEquals(amount, paymentRequest.getAmount());
    assertEquals(status, paymentRequest.getStatus());
    assertEquals(paymentKey, paymentRequest.getPaymentKey());
    assertEquals(approvedAt, paymentRequest.getApprovedAt());
  }

  @Test
  void testGetters() {
    // Given
    Long orderId = 1L;
    Long method = 1234L;
    Integer amount = 5000;
    String status = "APPROVED";
    String paymentKey = "paymentKey123";
    String approvedAt = "2024-12-09T15:30:00";
    PaymentRequest paymentRequest = new PaymentRequest(orderId, method, amount, status, paymentKey, approvedAt);

    // Then
    assertNotNull(paymentRequest.getOrderId());
    assertNotNull(paymentRequest.getMethod());
    assertNotNull(paymentRequest.getAmount());
    assertNotNull(paymentRequest.getStatus());
    assertNotNull(paymentRequest.getPaymentKey());
    assertNotNull(paymentRequest.getApprovedAt());
  }
}
