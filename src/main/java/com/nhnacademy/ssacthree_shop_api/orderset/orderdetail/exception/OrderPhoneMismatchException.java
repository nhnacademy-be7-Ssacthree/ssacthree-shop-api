package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception;

import org.springframework.http.HttpStatus;

// 목적: 주문의 구매자 전화번호와 입력 된 전화번호가 일치하지 않을 때 발생시킬 예외
public class OrderPhoneMismatchException extends OrderDetailCustomException {

  public OrderPhoneMismatchException(String phone) {
    super("일치하지 않는 전화번호: "+ phone, HttpStatus.BAD_REQUEST);

  }
}
