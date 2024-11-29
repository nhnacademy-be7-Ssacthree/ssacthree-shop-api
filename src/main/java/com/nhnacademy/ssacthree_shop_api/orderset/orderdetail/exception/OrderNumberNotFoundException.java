package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class OrderNumberNotFoundException extends OrderDetailCustomException {

  public OrderNumberNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
