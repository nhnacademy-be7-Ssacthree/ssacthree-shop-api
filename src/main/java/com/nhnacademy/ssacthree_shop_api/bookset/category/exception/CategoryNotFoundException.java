package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(long categoryId) {
        super("categoryId: " + categoryId + "과 일치하는 카테고리가 존재하지 않습니다.");
    }
}
