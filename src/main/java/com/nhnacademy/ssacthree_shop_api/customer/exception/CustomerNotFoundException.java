package com.nhnacademy.ssacthree_shop_api.customer.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
