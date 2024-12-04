package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookMgmtRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Repository
@RequiredArgsConstructor
public class BookMgmtRepositoryImpl implements BookMgmtRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBook book = QBook.book;
    private static final QPublisher publisher = QPublisher.publisher;
    private static final QBookCategory bookCategory = QBookCategory.bookCategory;
    private static final QBookTag bookTag = QBookTag.bookTag;
    private static final QBookAuthor bookAuthor = QBookAuthor.bookAuthor;
    private static final QCategory category = QCategory.category;
    private static final QTag tag = QTag.tag;
    private static final QAuthor author = QAuthor.author;
    private static final String BOOK_ID = "bookId";
    private static final String BOOK_NAME = "bookName";
    private static final String BOOK_ISBN = "bookIsbn";

    private BookRepository bookRepository;

    @Override
    public Page<BookSearchResponse> findAllBooks(Pageable pageable) {
        List<BookSearchResponse> books = queryFactory
            .select(Projections.constructor(BookSearchResponse.class,
                book.bookId,
                book.bookName,
                book.bookInfo,
                book.bookStatus.stringValue()
            ))
            .from(book)
            .where(book.bookStatus.ne(BookStatus.DELETE_BOOK))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifier(pageable))
            .fetch();

        books.forEach(b -> log.info("bookStatus 확인용: {}", b.getBookStatus()));

        // Fetch authors for each book
        books.forEach(b -> {
            List<AuthorNameResponse> authors = queryFactory
                .select(Projections.constructor(AuthorNameResponse.class, author.authorName))
                .from(bookAuthor)
                .join(author).on(author.authorId.eq(bookAuthor.author.authorId))
                .where(bookAuthor.book.bookId.eq(b.getBookId()))
                .fetch();
            b.setAuthors(authors);
        });

        long total = queryFactory
            .select(book.bookId.count())
            .from(book)
            .where(book.bookStatus.ne(BookStatus.DELETE_BOOK))
            .fetchOne();

        return new PageImpl<>(books, pageable, total);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable) {
        Sort sort = pageable.getSort(); // pageable에서 Sort 객체 가져오기

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            PathBuilder<Object> pathBuilder = new PathBuilder<>(book.getType(), "book"); // book의 타입과 엔티티를 사용
            OrderSpecifier<?> orderSpecifier = getOrderSpecifierForField(order, pathBuilder);
            orders.add(orderSpecifier);
        }
        return orders.toArray(new OrderSpecifier[0]);
    }



    private OrderSpecifier<?> getOrderSpecifierForField(Sort.Order order, PathBuilder<Object> pathBuilder) {
        String property = order.getProperty();

        if (property.equals(BOOK_ID)) {
            return order.isDescending()
                ? pathBuilder.getNumber(BOOK_ID, Long.class).desc()
                : pathBuilder.getNumber(BOOK_ID, Long.class).asc();
        } else if (property.equals(BOOK_NAME)) {
            return order.isDescending()
                ? pathBuilder.getString(BOOK_NAME).desc()
                : pathBuilder.getString(BOOK_NAME).asc();
        } else if (property.equals(BOOK_ISBN)) {
            return order.isDescending()
                ? pathBuilder.getNumber(BOOK_ISBN, Integer.class).desc()
                : pathBuilder.getNumber(BOOK_ISBN, Integer.class).asc();
        } else {
            return null;
        }
    }


}