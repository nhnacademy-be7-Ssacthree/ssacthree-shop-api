package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.springframework.http.HttpStatus;

public class OrderNumberNotFoundException extends OrderDetailCustomException {

  public OrderNumberNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
