package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class CategoryLimitExceededException extends CustomException {
    public CategoryLimitExceededException() {
        super("카테고리 설정 가능 개수를 초과했습니다. 최대 10개까지 설정 가능합니다.", 400);
    }
}
