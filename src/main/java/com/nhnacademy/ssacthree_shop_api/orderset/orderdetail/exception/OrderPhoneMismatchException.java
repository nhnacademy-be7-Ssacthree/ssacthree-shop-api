package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.springframework.http.HttpStatus;

public class OrderPhoneMismatchException extends OrderDetailCustomException {

  public OrderPhoneMismatchException(String message) {
    super(message, HttpStatus.BAD_REQUEST);

  }
}
