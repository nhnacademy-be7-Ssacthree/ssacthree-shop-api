package com.nhnacademy.ssacthree_shop_api.commons.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.ErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.OrderDetailErrorResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.exception.OrderDetailCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 직접 만든 커스텀 exception들
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(OrderDetailCustomException.class)
    public ResponseEntity<OrderDetailErrorResponse> orderDetailhandleCustomExcetion(OrderDetailCustomException e){
        OrderDetailErrorResponse errorResponse = new OrderDetailErrorResponse(
            e.getMessage(), e.getStatus()  // 메시지와 HttpStatus 전달
        );
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

}
