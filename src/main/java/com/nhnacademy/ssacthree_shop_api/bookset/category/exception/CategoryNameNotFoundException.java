package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class CategoryNameNotFoundException extends CustomException {
    public CategoryNameNotFoundException(String categoryName) {
        super("\""+categoryName+"\"과 일치하는 카테고리가 없습니다.", 404);
    }
}
