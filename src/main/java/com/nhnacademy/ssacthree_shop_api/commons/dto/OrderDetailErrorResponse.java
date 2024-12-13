package com.nhnacademy.ssacthree_shop_api.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class OrderDetailErrorResponse {
  private final String message;
  private final HttpStatus status;
}
