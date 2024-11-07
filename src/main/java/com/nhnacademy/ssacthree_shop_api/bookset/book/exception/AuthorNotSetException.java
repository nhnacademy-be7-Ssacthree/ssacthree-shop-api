package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

public class AuthorNotSetException extends RuntimeException {
    public AuthorNotSetException() {
        super("작가가 설정되지 않았습니다. 작가를 설정해주세요.");
    }
}
