package com.nhnacademy.ssacthree_shop_api.bookset.category.service;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;

public interface CategoryService {
    // 카테고리 저장
    CategoryInfoResponse saveCategory(CategorySaveRequest categorySaveRequest);

    // 카테고리 수정
    CategoryInfoResponse updateCategory(long categoryId, CategorySaveRequest categorySaveRequest);

    // 카테고리 soft 삭제
    // todo: 데이터베이스에 카테고리 사용여부 만들어야함.
    boolean deleteCategory(long CategoryId);
}
