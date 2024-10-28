package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class SubCategoryInUseException extends RuntimeException {
    public SubCategoryInUseException(long categoryId) {
        super("카테고리 ID " + categoryId + "에 사용 중인 하위 카테고리가 있어 사용 여부를 삭제할 수 없습니다.");
    }
}
