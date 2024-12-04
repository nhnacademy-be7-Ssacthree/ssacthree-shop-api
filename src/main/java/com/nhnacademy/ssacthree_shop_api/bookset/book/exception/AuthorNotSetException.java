package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class AuthorNotSetException extends CustomException {
    public AuthorNotSetException() {
        super("작가가 설정되지 않았습니다. 작가를 설정해주세요.", 400);
    }
}
