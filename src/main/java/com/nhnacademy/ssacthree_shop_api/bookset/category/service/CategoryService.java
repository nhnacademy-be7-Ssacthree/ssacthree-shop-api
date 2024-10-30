package com.nhnacademy.ssacthree_shop_api.bookset.category.service;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryInfoResponse;

import java.util.List;

public interface CategoryService {
    // 카테고리 저장
    CategoryInfoResponse saveCategory(CategorySaveRequest categorySaveRequest);

    // 카테고리 수정
    CategoryInfoResponse updateCategory(Long categoryId, CategoryUpdateRequest categoryUpdateRequest);

    // 카테고리 soft 삭제
    boolean deleteCategory(long categoryId);

    /**
     * 전체 카테고리 트리를 조회합니다.
     *
     * @return 최상위 카테고리 목록
     */
    List<CategoryInfoResponse> getAllCategories();

    /**
     * 특정 ID를 가진 카테고리를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 조회된 카테고리
     */
    CategoryInfoResponse getCategoryById(long categoryId);

    /**
     * 주어진 부모 카테고리 ID에 대한 자식 카테고리를 조회합니다.
     *
     * @param parentCategoryId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    List<CategoryInfoResponse> getChildCategories(long parentCategoryId);

    /**
     * 최상위 카테고리를 조회합니다.
     *
     * @return 최상위 카테고리 목록
     */
    List<CategoryInfoResponse> getRootCategories();

    /**
     * 이름을 기준으로 카테고리를 검색합니다.
     *
     * @param name 카테고리 이름
     * @return 검색된 카테고리 목록
     */
    List<CategoryInfoResponse> searchCategoriesByName(String name);

    /**
     * 특정 카테고리의 경로를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 최상위 카테고리까지의 경로 목록
     */
    List<CategoryInfoResponse> getCategoryPath(long categoryId);

    /**
     * 특정 카테고리의 자식 카테고리를 지정된 깊이까지 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param depth 조회 깊이
     * @return 자식 카테고리 목록
     */
    List<CategoryInfoResponse> getCategoryWithChildren(long categoryId, int depth);

    /**
     * 특정 카테고리의 모든 하위 카테고리(자식 및 자손)를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 모든 하위 카테고리 목록
     */
    List<CategoryInfoResponse> getAllDescendants(long categoryId);
}
