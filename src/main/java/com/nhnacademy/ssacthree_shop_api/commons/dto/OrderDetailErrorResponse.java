package com.nhnacademy.ssacthree_shop_api.commons.dto;

import org.springframework.http.HttpStatus;

public class OrderDetailErrorResponse {
  private final String message;
  private final HttpStatus status;

  public OrderDetailErrorResponse(String message, HttpStatus status) {
    this.message = message;
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
