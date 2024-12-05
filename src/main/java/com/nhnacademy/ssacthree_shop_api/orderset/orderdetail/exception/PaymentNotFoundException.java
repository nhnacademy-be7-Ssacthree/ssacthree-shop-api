package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends OrderDetailCustomException {

  public PaymentNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
