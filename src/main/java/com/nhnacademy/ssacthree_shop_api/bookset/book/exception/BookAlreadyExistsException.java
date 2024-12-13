package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class BookAlreadyExistsException extends CustomException {
    public BookAlreadyExistsException(String isbn) {
        super("이미 존재하는 책입니다. 등록하실 수 없습니다. (ISBN: " + isbn + ")", 400);
    }
}
