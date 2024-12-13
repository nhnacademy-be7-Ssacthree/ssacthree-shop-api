package com.nhnacademy.ssacthree_shop_api.commons.exception;

public class ConflictException extends CustomException {

    public ConflictException(String message) {
        super(message, 409);
    }
}
