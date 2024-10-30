package com.nhnacademy.ssacthree_shop_api.bookset.category.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

    /**
     * 주어진 부모 카테고리에 속한 자식 카테고리 목록을 조회합니다.
     *
     * @param superCategory 부모 카테고리
     * @return 자식 카테고리 목록
     */
    List<Category> findBySuperCategory(Category superCategory);

    /**
     * 최상위 카테고리(부모가 없는 카테고리) 목록을 조회합니다.
     *
     * @return 최상위 카테고리 목록
     */
    List<Category> findBySuperCategoryIsNull();

    /**
     * 특정 상위 카테고리 아래에 같은 이름의 카테고리가 존재하는지 확인합니다.
     *
     * @param superCategory 상위 카테고리
     * @param categoryName  확인할 카테고리 이름
     * @return 같은 이름의 카테고리가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsBySuperCategoryAndCategoryName(Category superCategory, String categoryName);
}
