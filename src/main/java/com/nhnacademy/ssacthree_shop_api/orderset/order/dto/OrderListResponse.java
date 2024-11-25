package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 주문내역 조회를 위한 OrderResponse 입니다. (OrderResponseWithCount DTO에서 사용)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponse {
  private Long orderId;
  private LocalDate orderDate;
  private int totalPrice;
  private String orderStatus;
}
