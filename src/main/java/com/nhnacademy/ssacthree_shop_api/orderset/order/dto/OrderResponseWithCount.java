package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseWithCount {
  private List<OrderListResponse> orders; // 주문 데이터 리스트
  private long totalOrders;           // 전체 주문 수
}