package com.nhnacademy.ssacthree_shop_api.bookset.book.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class CategoryNotSetException extends CustomException {
    public CategoryNotSetException() {
        super("카테고리가 설정되지 않았습니다. 카테고리를 1개 이상 10개 이하로 설정하시오.", 400);
    }
}
