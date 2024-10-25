package com.nhnacademy.ssacthree_shop_api.bookset.category.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

/**
 * 복잡한 쿼리문을 위한
 */
public interface CategoryCustomRepository {

    // 특정 상위 카테고리에 속한 바로 아래 하위 카테고리들 조회
    List<Category> findChildCategoryByParentCategoryId(long parentCategoryId);

    // 하위 카테고리의 바로 위 카테고리 조회
    List<Category> findParentCategoryByCategoryId(long childCategoryId);

    // 최상위 카테고리에 속한 최하위 카테고리 조회
    List<Category> findLeafCategoriesByRootCategoryId(long rootCategoryId);

    // 최하위 카테고리의 최상위 카테고리 조회
    List<Category> findRootCategoryByLeafCategoryId(long leafCategoryId);

    // 특정 부모 카테고리의 하위 카테고리 트리 조회
    List<Category> findCategoryTreeByRootCategoryId(long rootCategoryId);

    // 특정 하위 카테고리의 상위 카테고리 트리 조회
    List<Category> findCategoryTreeByLeafCategoryId(long leafCategoryId);

    // todo: 하위 카테고리가 존재하는지

}
