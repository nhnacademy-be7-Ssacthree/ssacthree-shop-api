package com.nhnacademy.ssacthree_shop_api.commons.exception;

public class IllegalArgumentException extends CustomException {

  public IllegalArgumentException(String message) {
    super(message, 400);
  }
}
