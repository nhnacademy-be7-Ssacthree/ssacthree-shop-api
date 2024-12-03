package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookCategoryCustomRepositoryImpl implements BookCategoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBookCategory bookCategory = QBookCategory.bookCategory;
    private static final QBook book = QBook.book;
    private static final QCategory category = QCategory.category;


    @Override
    public List<Category> findBookCategoriesByBookId(Long bookId) {
        return queryFactory.select(category)
                .from(bookCategory)
                .where(bookCategory.book.bookId.eq(bookId).and(bookCategory.category.categoryIsUsed.eq(true)))
                .fetch();
    }
}
