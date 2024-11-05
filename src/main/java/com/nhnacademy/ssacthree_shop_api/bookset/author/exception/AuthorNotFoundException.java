package com.nhnacademy.ssacthree_shop_api.bookset.author.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
