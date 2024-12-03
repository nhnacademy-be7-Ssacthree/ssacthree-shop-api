package com.nhnacademy.ssacthree_shop_api.bookset.category.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;

import java.util.List;

/**
 * 복잡한 쿼리문을 위한
 */
public interface CategoryCustomRepository {

    /**
     * 카테고리 이름으로 카테고리를 검색합니다.
     *
     * @param name 카테고리 이름
     * @return 검색된 카테고리 목록
     */
    List<Category> findCategoriesByName(String name);

    /**
     * 특정 카테고리까지의 경로를 최상위 카테고리까지 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리 경로 목록
     */
    List<Category> findCategoryPath(Long categoryId);

    /**
     * 특정 카테고리의 모든 하위 카테고리(자식 및 자손)를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 모든 하위 카테고리 목록
     */
    List<Category> findAllDescendants(Long categoryId);

    /**
     * 카테고리 ID를 통해 카테고리 이름을 반환합니다.
     * @param categoryId 카테고리 ID
     * @return 카테고리 이름
     */
    String findCategoryNameByCategoryId(Long categoryId);
}
