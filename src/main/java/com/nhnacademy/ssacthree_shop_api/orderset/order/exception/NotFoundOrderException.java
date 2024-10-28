package com.nhnacademy.ssacthree_shop_api.orderset.order.exception;

public class NotFoundOrderException extends RuntimeException {
    public NotFoundOrderException(String message) {
        super(message);
    }
}
