package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;

import java.util.List;

public interface BookCategoryCustomRepository {
    // 책 id로 책의 카테고리 조회
    List<Category> findBookCategoriesByBookId(Long bookId);
}
