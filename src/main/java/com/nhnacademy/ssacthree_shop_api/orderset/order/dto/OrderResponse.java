package com.nhnacademy.ssacthree_shop_api.orderset.order.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
  private Long orderId;
  private LocalDate orderDate;
  private int totalPrice;

  @Getter
  private String orderStatus;
}