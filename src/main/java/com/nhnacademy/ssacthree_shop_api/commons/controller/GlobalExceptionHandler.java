package com.nhnacademy.ssacthree_shop_api.commons.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.ErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.OrderDetailErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderDetailCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getCode())
            .body(new ErrorResponse(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(OrderDetailCustomException.class)
    public ResponseEntity<OrderDetailErrorResponse> orderDetailhandleCustomExcetion(OrderDetailCustomException e){

        log.info("예외발생: 존재하지않는주문번호입니다.");

        OrderDetailErrorResponse errorResponse = new OrderDetailErrorResponse(
            e.getMessage(), e.getStatus()  // 메시지와 HttpStatus 전달
        );

        return ResponseEntity.status(e.getStatus()).body(errorResponse);

    }

}
