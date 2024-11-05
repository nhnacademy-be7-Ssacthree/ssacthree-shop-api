package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class CategoryNotUsableException extends RuntimeException {
    public CategoryNotUsableException(Long categoryId) {
        super("현재 카테고리는 사용 중이지 않으므로 사용 또는 수정이 불가합니다. (카테고리 ID: " + categoryId + ")");
    }
}
