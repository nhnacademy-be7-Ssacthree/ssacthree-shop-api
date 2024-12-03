package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class CategoryDepthNotFoundException extends CustomException {
    public CategoryDepthNotFoundException(int depth) {
        super("깊이 "+ depth +"에서 카테고리가 존재하지 않습니다.", 404);
    }
}
