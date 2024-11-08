package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

public class CategoryLimitExceededException extends RuntimeException {
    public CategoryLimitExceededException() {
        super("카테고리 설정 가능 개수를 초과했습니다. 최대 10개까지 설정 가능합니다.");
    }
}
