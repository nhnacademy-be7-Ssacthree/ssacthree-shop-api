package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class BookNotFoundException extends CustomException {

    public BookNotFoundException(String string) {
        super(string, 404);
    }
}
