package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategoryId;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryCustomRepository {

}
