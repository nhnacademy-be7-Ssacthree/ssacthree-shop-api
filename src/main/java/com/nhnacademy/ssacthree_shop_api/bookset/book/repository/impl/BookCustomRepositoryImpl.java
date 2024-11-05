package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.querydsl.core.types.Projections;
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
    private static final QCategory category = QCategory.category;
    private static final QBookCategory bookCategory = QBookCategory.bookCategory;
    private static final QTag tag = QTag.tag;
    private static final QBookTag bookTag = QBookTag.bookTag;
    private static final QAuthor author = QAuthor.author;
    private static final QBookAuthor bookAuthor = QBookAuthor.bookAuthor;

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
                .join(book.publisher, publisher)
                .leftJoin(bookCategory).on(bookCategory.book.bookId.eq(book.bookId))
                .leftJoin(category).on(bookCategory.category.categoryId.eq(category.categoryId))
                .leftJoin(bookTag).on(bookTag.book.bookId.eq(book.bookId))
                .leftJoin(tag).on(bookTag.tag.tagId.eq(tag.tagId))
                .leftJoin(bookAuthor).on(bookAuthor.book.bookId.eq(book.bookId))
                .leftJoin(author).on(bookAuthor.author.authorId.eq(author.authorId))
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
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName,
                        category.categoryName.as("categoryName"),
                        tag.tagName.as("tagName"),
                        author.authorName.as("authorName")
                ))
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
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
                .join(book.publisher, publisher)
                .leftJoin(bookCategory).on(bookCategory.book.bookId.eq(book.bookId))
                .leftJoin(category).on(bookCategory.category.categoryId.eq(category.categoryId))
                .leftJoin(bookTag).on(bookTag.book.bookId.eq(book.bookId))
                .leftJoin(tag).on(bookTag.tag.tagId.eq(tag.tagId))
                .leftJoin(bookAuthor).on(bookAuthor.book.bookId.eq(book.bookId))
                .leftJoin(author).on(bookAuthor.author.authorId.eq(author.authorId))
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
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName,
                        category.categoryName.as("categoryName"),
                        tag.tagName.as("tagName"),
                        author.authorName.as("authorName")
                ))
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)), book.bookName.containsIgnoreCase(bookName))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)), book.bookName.containsIgnoreCase(bookName))
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
    public Page<BookInfoResponse> findAllAvailableBooks(Pageable pageable) {
        List<BookInfoResponse> books = queryFactory
                .from(book)
                .join(book.publisher, publisher)
                .leftJoin(bookCategory).on(bookCategory.book.bookId.eq(book.bookId))
                .leftJoin(category).on(bookCategory.category.categoryId.eq(category.categoryId))
                .leftJoin(bookTag).on(bookTag.book.bookId.eq(book.bookId))
                .leftJoin(tag).on(bookTag.tag.tagId.eq(tag.tagId))
                .leftJoin(bookAuthor).on(bookAuthor.book.bookId.eq(book.bookId))
                .leftJoin(author).on(bookAuthor.author.authorId.eq(author.authorId))
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
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName,
                        category.categoryName.as("categoryName"),
                        tag.tagName.as("tagName"),
                        author.authorName.as("authorName")
                ))
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
                .orderBy(book.publicationDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(book.count())
                .from(book)
                .where(book.bookStatus.eq(BookStatus.ON_SALE).or(book.bookStatus.eq(BookStatus.NO_STOCK)))
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
                .join(book.publisher, publisher)
                .leftJoin(bookCategory).on(bookCategory.book.bookId.eq(book.bookId))
                .leftJoin(category).on(bookCategory.category.categoryId.eq(category.categoryId))
                .leftJoin(bookTag).on(bookTag.book.bookId.eq(book.bookId))
                .leftJoin(tag).on(bookTag.tag.tagId.eq(tag.tagId))
                .leftJoin(bookAuthor).on(bookAuthor.book.bookId.eq(book.bookId))
                .leftJoin(author).on(bookAuthor.author.authorId.eq(author.authorId))
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
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName,
                        category.categoryName.as("categoryName"),
                        tag.tagName.as("tagName"),
                        author.authorName.as("authorName")
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
                .join(book.publisher, publisher)
                .leftJoin(bookCategory).on(bookCategory.book.bookId.eq(book.bookId))
                .leftJoin(category).on(bookCategory.category.categoryId.eq(category.categoryId))
                .leftJoin(bookTag).on(bookTag.book.bookId.eq(book.bookId))
                .leftJoin(tag).on(bookTag.tag.tagId.eq(tag.tagId))
                .leftJoin(bookAuthor).on(bookAuthor.book.bookId.eq(book.bookId))
                .leftJoin(author).on(bookAuthor.author.authorId.eq(author.authorId))
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
                        book.bookStatus.stringValue().as("bookStatus"),
                        book.publisher.publisherName,
                        category.categoryName.as("categoryName"),
                        tag.tagName.as("tagName"),
                        author.authorName.as("authorName")
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
