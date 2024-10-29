package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

public class InvalidCategoryDepthException extends RuntimeException {
    public InvalidCategoryDepthException(int depth, int maxDepth) {
        super(depth+"는 유효 깊이가 아닙니다. 유효 깊이는 1 또는 "+ maxDepth +"로 지정 가능합니다.");
    }
}