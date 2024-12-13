package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.springframework.http.HttpStatus;

// 목적: 주문번호로 조회 했는데 해당 주문이 없을 때
public class OrderNotFoundException extends OrderDetailCustomException {

  public OrderNotFoundException(Long orderId) {
    super("해당 주문을 찾지 못함 / 주문번호: " + orderId, HttpStatus.NOT_FOUND);
  }
}