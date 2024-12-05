package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class InvalidCategoryNameException extends CustomException {
    public InvalidCategoryNameException(String message) {
        super(message, 400);
    }
}
