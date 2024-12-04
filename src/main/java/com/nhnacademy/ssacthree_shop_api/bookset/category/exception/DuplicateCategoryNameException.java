package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.CustomException;

public class DuplicateCategoryNameException extends CustomException {
    public DuplicateCategoryNameException(String message) {
        super(message+" 다른 이름으로 설정하세요.", 400);
    }
}
