package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

// 주문내역 조회를 위한 OrderResponse 입니다. (OrderResponseWithCount DTO에서 사용)
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class OrderListResponse {
  private Long orderId;
  private LocalDate orderDate;
  private int totalPrice;
  private String orderStatus;

  public OrderListResponse(Long orderId, LocalDate orderDate, int totalPrice, String orderStatus) {
    this.orderId = orderId;
    this.orderDate = orderDate;
    this.totalPrice = totalPrice;
    this.orderStatus = convertOrderStatus(orderStatus);
  }

  // orderStatus 값을 변환하는 메서드
  private String convertOrderStatus(String status) {
    switch (status) {
      case "PENDING":
        return "대기";
      case "IN_SHOPPING":
        return "배송중";
      case "COMPLETED":
        return "완료";
      case "RETURNED":
        return "반품";
      case "CANCELED":
        return "주문취소";
      default:
        throw new IllegalArgumentException("주문상태 변환오류: " + status);
    }
  }
}
