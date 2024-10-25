package com.nhnacademy.ssacthree_shop_api.bookset.category.service;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;

import java.util.List;

public interface CategoryService {
    // 카테고리 저장
    CategoryInfoResponse saveCategory(CategorySaveRequest categorySaveRequest);

    // 카테고리 수정
    CategoryInfoResponse updateCategory(long categoryId, CategorySaveRequest categorySaveRequest);

    // 카테고리 soft 삭제
    boolean deleteCategory(long CategoryId);

    // 최상위 카테고리 검색
    List<CategoryInfoResponse> findBySuperCategoryIsNull();
    //List<CategoryInfoResponse>
    // 상위 카테고리 바로 아래 카테고리 검색
    // 하위 카테고리 바로 위 카테고리 검색
    // 하위 카테고리 최상 위 카테고리 검색
    // 하위 카테고리의 모든 상위 카테고리 검색(부모, 조상)
    // 최상위 카테고리에 속한 모든 카테고리 검색
    // 상위 카테고리의 하위 카테고리 트리 검색
    // 최상위 카테고리의 최하위 카테고리 검색 -> 국내 도서에 쿠폰을 적용할 때 '국내>문학>소설'일 경우 국내에 소속된 소설을 찾기 위한
    // 전체 카테고리 트리 검색
}
