package com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookAuthorCustomRepositoryImpl implements BookAuthorCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBookAuthor bookAuthor = QBookAuthor.bookAuthor;
    private static final QBook book = QBook.book;
    private static final QAuthor author = QAuthor.author;


    @Override
    public List<Author> findBookAuthorByBookId(Long bookId) {
        return queryFactory.select(author)
                .from(bookAuthor)
                .where(bookAuthor.book.bookId.eq(bookId))
                .fetch();
    }
}
