package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBook book = QBook.book;
    private static final QPublisher publisher = QPublisher.publisher;

    BookStatusConverter converter = new BookStatusConverter();

    /**
     * 판매 중, 재고 없음 상태의 책을 최신 출판 순으로 불러옴.
     * @param pageable 페이징 설정
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findRecentBooks(Pageable pageable) {
        List<BookInfoResponse> books = queryFactory
                .from(book)
                .innerJoin(publisher).on(book.publisher.publisherId.eq(publisher.publisherId))
                .select(Projections.fields(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        Expressions.stringTemplate("'{0}'", book.bookStatus).as("bookStatus"),
                        book.publisher.publisherName
                ))
                .where(book.bookStatus.eq(BookStatus.ON_SALE), book.bookStatus.eq(BookStatus.NO_STOCK))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE), book.bookStatus.eq(BookStatus.NO_STOCK))
                .fetchOne();

        count = (count == null ? 0 : count);

        return  new PageImpl<>(books, pageable, count);
    }

    /**
     * 책 이름을 포함하고 있는 책을 검색합니다.
     * @param pageable 페이징 처리
     * @param bookName 찾고자 하는 책 이름
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findBooksByBookName(Pageable pageable, String bookName) {
        List<BookInfoResponse> books = queryFactory
                .from(book)
                .innerJoin(publisher).on(book.publisher.publisherId.eq(publisher.publisherId))
                .select(Projections.fields(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        Expressions.stringTemplate("'{0}'", book.bookStatus).as("bookStatus"),
                        book.publisher.publisherName
                ))
                .where(book.bookStatus.eq(BookStatus.ON_SALE), book.bookStatus.eq(BookStatus.NO_STOCK), book.bookName.containsIgnoreCase(bookName))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE), book.bookStatus.eq(BookStatus.NO_STOCK), book.bookName.containsIgnoreCase(bookName))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 판매 중, 재고 없는 상태의 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findAllBooksByStatusOnSale(Pageable pageable) {
        List<BookInfoResponse> books = queryFactory
                .from(book)
                .innerJoin(publisher).on(book.publisher.publisherId.eq(publisher.publisherId))
                .select(Projections.fields(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        Expressions.stringTemplate("'{0}'", book.bookStatus).as("bookStatus"),
                        book.publisher.publisherName
                ))
                .where(book.bookStatus.eq(BookStatus.ON_SALE), book.bookStatus.eq(BookStatus.NO_STOCK))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE), book.bookStatus.eq(BookStatus.NO_STOCK))
                .fetchOne();

        count = (count == null ? 0 : count);

        return  new PageImpl<>(books, pageable, count);
    }

    /**
     * 재고가 없는 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findAllBooksByStatusNoStock(Pageable pageable) {
        List<BookInfoResponse> books = queryFactory
                .from(book)
                .innerJoin(publisher).on(book.publisher.publisherId.eq(publisher.publisherId))
                .select(Projections.fields(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        Expressions.stringTemplate("'{0}'", book.bookStatus).as("bookStatus"),
                        book.publisher.publisherName
                ))
                .where(book.bookStatus.eq(BookStatus.NO_STOCK))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.NO_STOCK))
                .fetchOne();

        count = (count == null ? 0 : count);

        return  new PageImpl<>(books, pageable, count);
    }

    /**
     * 판매 중단된 책을 모두 검색합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookInfoResponse> findStatusDiscontinued(Pageable pageable) {
        List<BookInfoResponse> books = queryFactory
                .from(book)
                .innerJoin(publisher).on(book.publisher.publisherId.eq(publisher.publisherId))
                .select(Projections.fields(BookInfoResponse.class,
                        book.bookId,
                        book.bookName,
                        book.bookIndex,
                        book.bookInfo,
                        book.bookIsbn,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.isPacked,
                        book.stock,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        Expressions.stringTemplate("'{0}'", book.bookStatus).as("bookStatus"),
                        book.publisher.publisherName
                ))
                .where(book.bookStatus.eq(BookStatus.DISCONTINUED))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.DISCONTINUED))
                .fetchOne();

        count = (count == null ? 0 : count);

        return  new PageImpl<>(books, pageable, count);
    }

    /**
     * 특정 책 정보를 가져옵니다.
     * @param bookId 책 아이디
     * @return 책 정보
     */
    @Override
    public String findBookInfoByBookId(Long bookId) {
        return queryFactory.select(book.bookInfo)
                .from(book)
                .where(book.bookId.eq(bookId))
                .fetchOne();
    }
}
