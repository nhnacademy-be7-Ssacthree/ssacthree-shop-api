package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderSaveRequestTest {

  @Test
  void testOrderSaveRequestConstructor() {
    // Given
    OrderDetailSaveRequest orderDetail1 = new OrderDetailSaveRequest(1L, 1001L, 2001L, 2, 100, 3001L);
    OrderDetailSaveRequest orderDetail2 = new OrderDetailSaveRequest(2L, 1001L, 2002L, 1, 150, 3002L);
    Long customerId = 123L;
    String buyerName = "John Doe";
    String buyerEmail = "john.doe@example.com";
    String buyerPhone = "123-456-7890";
    String recipientName = "Jane Doe";
    String recipientPhone = "098-765-4321";
    String postalCode = "12345";
    String roadAddress = "123 Main St.";
    String detailAddress = "Apt 101";
    String orderRequest = "Please deliver on weekends";
    LocalDate deliveryDate = LocalDate.of(2024, 12, 15);
    Integer pointToUse = 100;
    Integer pointToSave = 200;
    Integer totalPrice = 500;
    Long deliveryRuleId = 1L;
    String orderNumber = "ORD12345";

    // When
    OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
        Arrays.asList(orderDetail1, orderDetail2),
        customerId,
        buyerName,
        buyerEmail,
        buyerPhone,
        recipientName,
        recipientPhone,
        postalCode,
        roadAddress,
        detailAddress,
        orderRequest,
        deliveryDate,
        pointToUse,
        pointToSave,
        totalPrice,
        deliveryRuleId,
        orderNumber
    );

    // Then
    assertNotNull(orderSaveRequest);  // Verify the object is not null

    // Grouped assertions for order details
    assertAll("Order details validation",
        () -> assertEquals(2, orderSaveRequest.getOrderDetailList().size()),
        () -> verifyOrderDetail(orderSaveRequest.getOrderDetailList().get(0), 1L, 1001L, 2001L, 2, 100, 3001L),
        () -> verifyOrderDetail(orderSaveRequest.getOrderDetailList().get(1), 2L, 1001L, 2002L, 1, 150, 3002L)
    );

    // Group other field validations
    assertAll("OrderSaveRequest field validations",
        () -> assertEquals(customerId, orderSaveRequest.getCustomerId()),
        () -> assertEquals(buyerName, orderSaveRequest.getBuyerName()),
        () -> assertEquals(buyerEmail, orderSaveRequest.getBuyerEmail()),
        () -> assertEquals(buyerPhone, orderSaveRequest.getBuyerPhone()),
        () -> assertEquals(recipientName, orderSaveRequest.getRecipientName()),
        () -> assertEquals(recipientPhone, orderSaveRequest.getRecipientPhone()),
        () -> assertEquals(postalCode, orderSaveRequest.getPostalCode()),
        () -> assertEquals(roadAddress, orderSaveRequest.getRoadAddress()),
        () -> assertEquals(detailAddress, orderSaveRequest.getDetailAddress()),
        () -> assertEquals(orderRequest, orderSaveRequest.getOrderRequest()),
        () -> assertEquals(deliveryDate, orderSaveRequest.getDeliveryDate()),
        () -> assertEquals(pointToUse, orderSaveRequest.getPointToUse()),
        () -> assertEquals(pointToSave, orderSaveRequest.getPointToSave()),
        () -> assertEquals(totalPrice, orderSaveRequest.getTotalPrice()),
        () -> assertEquals(deliveryRuleId, orderSaveRequest.getDeliveryRuleId()),
        () -> assertEquals(orderNumber, orderSaveRequest.getOrderNumber())
    );
  }

  // Helper method to verify OrderDetailSaveRequest
  private void verifyOrderDetail(OrderDetailSaveRequest orderDetail, Long expectedBookId, Long expectedOrderId, Long expectedCouponId,
      Integer expectedQuantity, Integer expectedBookPriceAtOrder, Long expectedPackagingId) {
    assertEquals(expectedBookId, orderDetail.getBookId());
    assertEquals(expectedOrderId, orderDetail.getOrderId());
    assertEquals(expectedCouponId, orderDetail.getCouponId());
    assertEquals(expectedQuantity, orderDetail.getQuantity());
    assertEquals(expectedBookPriceAtOrder, orderDetail.getBookPriceAtOrder());
    assertEquals(expectedPackagingId, orderDetail.getPackagingId());
  }
}

