package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategoryId;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {
    //todo: custom repository에서 구현해야 할듯
    // 책 id로 책의 카테고리 조회
    List<Category> findBookCategoriesByBookId(Long bookId);
}
