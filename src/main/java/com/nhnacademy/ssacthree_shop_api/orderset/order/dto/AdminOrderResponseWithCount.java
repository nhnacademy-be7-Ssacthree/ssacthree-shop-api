package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderResponseWithCount {
  private List<AdminOrderListResponse> orders; // 주문 데이터 리스트
  private long totalOrders;           // 전체 주문 수
}