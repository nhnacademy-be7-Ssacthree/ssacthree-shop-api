package com.nhnacademy.ssacthree_shop_api.commons.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.ErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.OrderDetailErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderDetailCustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getCode())
            .body(new ErrorResponse(e.getMessage(), e.getCode()));
    }

    @ExceptionHandler(OrderDetailCustomException.class)
    public ResponseEntity<OrderDetailErrorResponse> OrderDetailhandleCustomExcetion(OrderDetailCustomException e){
        return ResponseEntity.status(e.getStatus()).body(new OrderDetailErrorResponse(e.getMessage(), e.getStatus()));
    }

}
