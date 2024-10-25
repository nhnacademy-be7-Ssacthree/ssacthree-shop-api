package com.nhnacademy.ssacthree_shop_api.bookset.category.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryCustomRepository {

    // 카테고리 이름을 가진 카테고리 목록
    List<Category> findByCategoryName(String categoryName);

    // 상위 카테고리가 없는 최상위 카테고리 조회
    List<Category> findBySuperCategoryIsNull();

    boolean existsByCategoryName(String categoryName);

}
