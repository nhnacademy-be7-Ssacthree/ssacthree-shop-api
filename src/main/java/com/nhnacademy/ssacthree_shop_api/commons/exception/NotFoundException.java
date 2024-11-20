package com.nhnacademy.ssacthree_shop_api.commons.exception;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message, 404);
    }
}
