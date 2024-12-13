package com.nhnacademy.ssacthree_shop_api.orderset.payment.dto;


import com.nhnacademy.ssacthree_shop_api.orderset.payment.domain.dto.PaymentCancelRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentCancelRequestTest {

  private PaymentCancelRequest paymentCancelRequest;

  @BeforeEach
  void setUp() {
    // Create a PaymentCancelRequest using the constructor with arguments
    paymentCancelRequest = new PaymentCancelRequest("PAY12345", "Customer requested cancellation");
  }

  @Test
  void testDefaultConstructor() {
    // Test the default constructor
    PaymentCancelRequest defaultRequest = new PaymentCancelRequest();
    assertNotNull(defaultRequest);
    assertNull(defaultRequest.getPaymentKey());
    assertNull(defaultRequest.getCancelReason());
  }

  @Test
  void testConstructorWithArguments() {
    // Test the constructor with arguments
    assertNotNull(paymentCancelRequest);
    assertEquals("PAY12345", paymentCancelRequest.getPaymentKey());
    assertEquals("Customer requested cancellation", paymentCancelRequest.getCancelReason());
  }

  @Test
  void testSettersAndGetters() {
    // Test setters and getters
    paymentCancelRequest.setPaymentKey("PAY54321");
    paymentCancelRequest.setCancelReason("Order was mistaken");

    assertEquals("PAY54321", paymentCancelRequest.getPaymentKey());
    assertEquals("Order was mistaken", paymentCancelRequest.getCancelReason());
  }

  @Test
  void testPaymentKey() {
    // Test that paymentKey is correctly set
    paymentCancelRequest.setPaymentKey("PAY67890");
    assertEquals("PAY67890", paymentCancelRequest.getPaymentKey());
  }

  @Test
  void testCancelReason() {
    // Test that cancelReason is correctly set
    paymentCancelRequest.setCancelReason("Payment was declined");
    assertEquals("Payment was declined", paymentCancelRequest.getCancelReason());
  }
}

