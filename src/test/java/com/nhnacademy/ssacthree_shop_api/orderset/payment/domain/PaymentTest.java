package com.nhnacademy.ssacthree_shop_api.orderset.payment.domain;


import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

  private Payment mockPayment;
  private PaymentType mockPaymentType;
  private Order mockOrder;

  @BeforeEach
  void setUp() {
    // Setting up mock objects for Order and PaymentType
    mockPaymentType = new PaymentType(1L, "Credit Card", true, LocalDateTime.now());
    mockOrder = new Order(1L, null, null, LocalDateTime.now(), 10000, "ORD12345", null, "John Doe", "010-1234-5678", "12345", "Road Address", "Detail Address", "No request", null, "Invoice123");

    // Creating Payment object
    mockPayment = new Payment(
        1L,
        mockOrder,
        mockPaymentType,
        LocalDateTime.now(),
        10000,
        "PAY12345",
        PaymentStatusEnum.DONE
    );
  }

  @Test
  void testPaymentInitialization() {
    // Test to ensure that Payment is initialized correctly
    assertNotNull(mockPayment);
    assertEquals(1L, mockPayment.getId());
    assertEquals(mockOrder, mockPayment.getOrder());
    assertEquals(mockPaymentType, mockPayment.getPaymentType());
    assertEquals(10000, mockPayment.getPaymentAmount());
    assertEquals("PAY12345", mockPayment.getPaymentKey());
    assertEquals(PaymentStatusEnum.DONE, mockPayment.getPaymentStatus());
  }

  @Test
  void testSetPaymentStatus() {
    // Test if setter for paymentStatus works correctly
    mockPayment.setPaymentStatus(PaymentStatusEnum.CANCEL);
    assertEquals(PaymentStatusEnum.CANCEL, mockPayment.getPaymentStatus());
  }

  @Test
  void testPaymentCreatedAt() {
    // Test that paymentCreatedAt is set properly in the constructor
    assertNotNull(mockPayment.getPaymentCreatedAt());
  }

  @Test
  void testPaymentTypeAssociation() {
    // Test that the PaymentType is correctly associated with the Payment
    assertEquals(mockPaymentType, mockPayment.getPaymentType());
  }

  @Test
  void testOrderAssociation() {
    // Test that the Order is correctly associated with the Payment
    assertEquals(mockOrder, mockPayment.getOrder());
  }
}
