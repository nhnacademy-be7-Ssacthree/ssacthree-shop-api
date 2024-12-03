package com.nhnacademy.ssacthree_shop_api.bookset.category.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

    /**
     * 주어진 부모 카테고리에 속한 자식 카테고리 목록을 조회합니다.
     *
     * @param superCategory 부모 카테고리
     * @return 자식 카테고리 목록
     */
    List<Category> findBySuperCategoryAndCategoryIsUsedTrue(Category superCategory);


    /**
     * 특정 상위 카테고리 아래에 같은 이름의 카테고리가 존재하는지 확인합니다.
     *
     * @param superCategory 상위 카테고리
     * @param categoryName  설정하고자 하는 카테고리 이름
     * @return 같은 상위 카테고리 아래 설정하고자 하는 이름을 가진 카테고리
     */
    Category findBySuperCategoryAndCategoryNameAndCategoryIsUsedTrue(Category superCategory, String categoryName);

    Category findByCategoryIdAndCategoryIsUsedTrue(Long categoryId);
}
