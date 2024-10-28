package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class DuplicateCategoryNameException extends RuntimeException {
    public DuplicateCategoryNameException(String message) {
        super(message);
    }
}
