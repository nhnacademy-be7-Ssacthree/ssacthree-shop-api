package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QBook book = QBook.book;
    private static final QPublisher publisher = QPublisher.publisher;
    private static final QBookCategory bookCategory = QBookCategory.bookCategory;
    private static final QBookTag bookTag = QBookTag.bookTag;
    private static final QBookAuthor bookAuthor = QBookAuthor.bookAuthor;
    private static final QCategory category = QCategory.category;
    private static final QTag tag = QTag.tag;
    private static final QAuthor author = QAuthor.author;

    private BooleanExpression isOnSaleOrNoStock() {
        return book.bookStatus.eq(BookStatus.ON_SALE)
                .or(book.bookStatus.eq(BookStatus.NO_STOCK));
    }

    /**
     * 판매 중, 재고 없음 상태의 책을 최신 출판 순으로 불러옴.
     * @param pageable 페이징 설정
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookBaseResponse> findRecentBooks(Pageable pageable) {
        Map<Long, BookBaseResponse> bookMap = queryFactory
                .from(book)
                .leftJoin(book.publisher, publisher)
                .where(isOnSaleOrNoStock())
                .transform(groupBy(book.bookId).as(
                        Projections.constructor(BookBaseResponse.class,
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
                                book.bookStatus.stringValue(),
                                Projections.constructor(PublisherNameResponse.class,
                                        publisher.publisherId,
                                        publisher.publisherName)
                        )));

        List<BookBaseResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(isOnSaleOrNoStock())
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }


    /**
     * 책 이름을 포함하고 있는 책을 검색합니다.
     * @param pageable 페이징 처리
     * @param bookName 찾고자 하는 책 이름
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookBaseResponse> findBooksByBookName(Pageable pageable, String bookName) {
        Map<Long, BookBaseResponse> bookMap = queryFactory
                .from(book)
                .leftJoin(book.publisher, publisher)
                .where(isOnSaleOrNoStock()
                        .and(book.bookName.containsIgnoreCase(bookName)))
                .transform(groupBy(book.bookId).as(
                        Projections.constructor(BookBaseResponse.class,
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
                                book.bookStatus.stringValue(),
                                Projections.constructor(PublisherNameResponse.class,
                                        publisher.publisherId,
                                        publisher.publisherName)
                        )));

        List<BookBaseResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(isOnSaleOrNoStock(),
                        book.bookName.containsIgnoreCase(bookName))
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
    public Page<BookBaseResponse> findAllAvailableBooks(Pageable pageable) {
        Map<Long, BookBaseResponse> bookMap = queryFactory
                .from(book)
                .leftJoin(book.publisher, publisher)
                .where(isOnSaleOrNoStock())
                .transform(groupBy(book.bookId).as(
                        Projections.constructor(BookBaseResponse.class,
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
                                book.bookStatus.stringValue(),
                                Projections.constructor(PublisherNameResponse.class,
                                        publisher.publisherId,
                                        publisher.publisherName)
                        )));

        List<BookBaseResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(isOnSaleOrNoStock())
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 재고가 없는 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookBaseResponse> findAllBooksByStatusNoStock(Pageable pageable) {
        Map<Long, BookBaseResponse> bookMap = queryFactory
                .from(book)
                .leftJoin(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.NO_STOCK))
                .transform(groupBy(book.bookId).as(
                        Projections.constructor(BookBaseResponse.class,
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
                                book.bookStatus.stringValue(),
                                Projections.constructor(PublisherNameResponse.class,
                                        publisher.publisherId,
                                        publisher.publisherName)
                        )));

        List<BookBaseResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.NO_STOCK))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
    }

    /**
     * 판매 중단된 책을 모두 검색합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookBaseResponse> findStatusDiscontinued(Pageable pageable) {
        Map<Long, BookBaseResponse> bookMap = queryFactory
                .from(book)
                .leftJoin(book.publisher, publisher)
                .where(book.bookStatus.eq(BookStatus.DISCONTINUED))
                .transform(groupBy(book.bookId).as(
                        Projections.constructor(BookBaseResponse.class,
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
                                book.bookStatus.stringValue(),
                                Projections.constructor(PublisherNameResponse.class,
                                        publisher.publisherId,
                                        publisher.publisherName)
                        )));

        List<BookBaseResponse> books = new ArrayList<>(bookMap.values());

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.DISCONTINUED))
                .fetchOne();

        count = (count == null ? 0 : count);

        return new PageImpl<>(books, pageable, count);
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

    @Override
    public BookBaseResponse findByBookIsbn(String isbn) {
        return queryFactory.select(Projections.constructor(BookBaseResponse.class,
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
                        book.bookStatus.stringValue(),
                        Projections.constructor(PublisherNameResponse.class,
                                publisher.publisherId,
                                publisher.publisherName)
                ))
                .from(book)
                .leftJoin(book.publisher, publisher)
                .where(book.bookIsbn.eq(isbn))
                .fetchOne();
    }

    @Override
    public List<CategoryNameResponse> findCategoriesByBookId(Long bookId) {
        return queryFactory
                .select(Projections.constructor(CategoryNameResponse.class,
                        category.categoryId,
                        category.categoryName
                ))
                .from(bookCategory)
                .leftJoin(bookCategory.category, category)
                .where(bookCategory.book.bookId.eq(bookId))
                .fetch();
    }

    @Override
    public List<TagInfoResponse> findTagsByBookId(Long bookId) {
        return queryFactory
                .select(Projections.constructor(TagInfoResponse.class,
                        tag.tagId,
                        tag.tagName
                ))
                .from(bookTag)
                .leftJoin(bookTag.tag, tag)
                .where(bookTag.book.bookId.eq(bookId))
                .fetch();
    }

    @Override
    public List<AuthorNameResponse> findAuthorsByBookId(Long bookId) {
        return queryFactory
                .select(Projections.constructor(AuthorNameResponse.class,
                        author.authorId,
                        author.authorName
                ))
                .from(bookAuthor)
                .leftJoin(bookAuthor.author, author)
                .where(bookAuthor.book.bookId.eq(bookId))
                .fetch();
    }


    // 작가 아이디로 책 찾기
    @Override
    public Page<BookBaseResponse> findBooksByAuthorId(Long authorId, Pageable pageable) {
        List<BookBaseResponse> books = queryFactory.select(Projections.constructor(BookBaseResponse.class,
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
                book.bookStatus.stringValue(),
                Projections.constructor(PublisherNameResponse.class,
                        publisher.publisherId,
                        publisher.publisherName)
                ))
                .from(book)
                .leftJoin(book.publisher, publisher)
                .leftJoin(book.bookAuthors, bookAuthor)
                .leftJoin(bookAuthor.author, author)
                .where(author.authorId.eq(authorId).and(isOnSaleOrNoStock()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .leftJoin(book.bookAuthors, bookAuthor)
                .leftJoin(bookAuthor.author, author)
                .where(author.authorId.eq(authorId).and(isOnSaleOrNoStock()))
                .fetchOne();

        count = (count == null) ? 0 : count;

        return new PageImpl<>(books, pageable, count);
    }
}