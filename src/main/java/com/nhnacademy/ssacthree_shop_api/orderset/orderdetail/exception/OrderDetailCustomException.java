package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.springframework.http.HttpStatus;

public class OrderDetailCustomException extends RuntimeException {

  private final HttpStatus status;

  public OrderDetailCustomException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }

}
