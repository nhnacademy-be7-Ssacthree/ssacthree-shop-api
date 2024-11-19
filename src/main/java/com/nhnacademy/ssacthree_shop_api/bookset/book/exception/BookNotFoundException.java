package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String string) {
        super(string);
    }
}
