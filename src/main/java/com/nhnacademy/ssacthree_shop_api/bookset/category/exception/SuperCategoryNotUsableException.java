package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class SuperCategoryNotUsableException extends RuntimeException {
    public SuperCategoryNotUsableException(long categoryId) {
        super("상위 카테고리가 사용 중이지 않으므로 설정할 수 없습니다. (카테고리 ID: " + categoryId + ")");
    }
}
