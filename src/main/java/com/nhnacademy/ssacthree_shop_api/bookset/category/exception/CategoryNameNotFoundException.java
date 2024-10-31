package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class CategoryNameNotFoundException extends RuntimeException {
    public CategoryNameNotFoundException(String categoryName) {
        super("\""+categoryName+"\"과 일치하는 카테고리가 없습니다.");
    }
}
