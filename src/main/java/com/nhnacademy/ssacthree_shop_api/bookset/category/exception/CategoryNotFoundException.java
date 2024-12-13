package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class CategoryNotFoundException extends CustomException {
    public CategoryNotFoundException(long categoryId) {
        super("categoryId: " + categoryId + "과 일치하는 카테고리가 존재하지 않습니다.", 404);
    }

    public CategoryNotFoundException(String message) {
        super(message, 404);
    }
}
