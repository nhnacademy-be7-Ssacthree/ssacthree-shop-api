package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.dto.BookAuthorDto;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.dto.BookCategoryDto;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.dto.BookTagDto;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.commons.util.QueryDslSortUtil;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;

import java.util.List;
import java.util.stream.Collectors;

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

    private final CategoryRepository categoryRepository;

    /**
     * 도서 상태가 판매중이거나 재고 없음인지 판별합니다.
     * @return '판매중/재고없음'인지 아닌지에 대한 BooleanExpression
     */
    private BooleanExpression isOnSaleOrNoStock() {
        return book.bookStatus.eq(BookStatus.ON_SALE)
                .or(book.bookStatus.eq(BookStatus.NO_STOCK));

    }

    /**
     * 조인 조건을 동적으로 처리하기 위한 인터페이스
     */
    @FunctionalInterface
    private interface JoinClause {
        void apply(JPAQuery<?> query);
    }

    /**
     * 공통 메소드 처리
     * @param pageable 페이징 처리
     * @param condition where 조건절
     * @param joinConditions 조인 리스트
     * @return 도서 기본 정보 페이지
     */
    private Page<BookBaseResponse> findBooksByCondition(
            Pageable pageable,
            Predicate condition,
            List<JoinClause> joinConditions
    ) {
        JPAQuery<BookBaseResponse> query = queryFactory
                .select(Projections.constructor(BookBaseResponse.class,
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
                .leftJoin(book.publisher, publisher);

        // 동적 조인 조건 추가
        for (JoinClause join : joinConditions) {
            join.apply(query);
        }

        // 정렬 조건 적용
        PathBuilder pathBuilder = new PathBuilder<>(QBook.book.getType(), QBook.book.getMetadata());
        QueryDslSortUtil.applyOrderBy(query, pageable.getSort(), pathBuilder);

        // 페이징 처리 및 결과 조회
        List<BookBaseResponse> books = query
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count 쿼리
        JPAQuery<Long> countQuery = queryFactory.select(book.count())
                .from(book)
                .leftJoin(book.publisher, publisher);

        // Count에도 동적 조인 조건 추가
        for (JoinClause join : joinConditions) {
            join.apply(countQuery);
        }

        Long count = countQuery
                .where(condition)
                .fetchOne();

        count = count != null ? count : 0L; // Null 체크 후 기본값 설정


        return new PageImpl<>(books, pageable, count);
    }


    /**
     * 책 이름을 포함하고 있는 책을 검색합니다.
     * @param pageable 페이징 처리
     * @param bookName 찾고자 하는 책 이름
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookBaseResponse> findBooksByBookName(Pageable pageable, String bookName) {
        Predicate condition = isOnSaleOrNoStock().and(book.bookName.containsIgnoreCase(bookName));
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 판매 중, 재고 없는 상태의 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookBaseResponse> findAllAvailableBooks(Pageable pageable) {
        Predicate condition = isOnSaleOrNoStock();
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 재고가 없는 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookBaseResponse> findAllBooksByStatusNoStock(Pageable pageable) {
        Predicate condition = book.bookStatus.eq(BookStatus.NO_STOCK);
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 판매 중단된 책을 모두 검색합니다.
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookBaseResponse> findStatusDiscontinued(Pageable pageable) {
        Predicate condition = book.bookStatus.eq(BookStatus.DISCONTINUED);
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 작가 아이디로 작가의 도서 조회
     * @param authorId 작가 아이디
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookBaseResponse> findBooksByAuthorId(Long authorId, Pageable pageable) {
        List<JoinClause> joinConditions = List.of(
                query -> query.leftJoin(book.bookAuthors, bookAuthor).leftJoin(bookAuthor.author, author)
        );
        Predicate condition = author.authorId.eq(authorId).and(isOnSaleOrNoStock());
        return findBooksByCondition(pageable, condition, joinConditions);
    }

    /**
     * 태그 아이디로 도서 조회
     * @param tagId 태그 아이디
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookBaseResponse> findBooksByTagId(Long tagId, Pageable pageable) {
        List<JoinClause> joinConditions = List.of(
                query -> query.leftJoin(book.bookTags, bookTag).leftJoin(bookTag.tag, tag)
        );
        Predicate condition = tag.tagId.eq(tagId).and(isOnSaleOrNoStock());
        return findBooksByCondition(pageable, condition, joinConditions);
    }

    /**
     * 카테고리 아이디로 도서 조회
     * @param categoryId 카테고리 아이디
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookBaseResponse> findBooksByCategoryId(Long categoryId, Pageable pageable) {
//        List<JoinClause> joinConditions = List.of(
//                query -> query.leftJoin(book.bookCategories, bookCategory).leftJoin(bookCategory.category, category)
//        );
//        Predicate condition = category.categoryId.eq(categoryId).and(isOnSaleOrNoStock());
//        return findBooksByCondition(pageable, condition, joinConditions);
        // 현재 카테고리와 모든 하위 카테고리 ID 조회
        List<Long> allCategoryIds = categoryRepository.findAllDescendants(categoryId).stream()
                .map(Category::getCategoryId) // 하위 카테고리의 ID 리스트 추출
                .collect(Collectors.toList());
        allCategoryIds.add(categoryId); // 현재 카테고리 ID도 포함

        // 동적 조인 조건 추가
        List<JoinClause> joinConditions = List.of(
                query -> query.leftJoin(book.bookCategories, bookCategory).leftJoin(bookCategory.category, category)
        );

        // 다중 ID 조건 생성
        Predicate condition = category.categoryId.in(allCategoryIds).and(isOnSaleOrNoStock());

        // 조건과 조인으로 결과 조회
        return findBooksByCondition(pageable, condition, joinConditions);
    }

    /**
     * isbn으로 도서를 검색합니다.
     * @param isbn 도서 isbn
     * @return 도서 기본 정보
     */
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

    private synchronized void addViewCount(QBook book, Long bookId){
        queryFactory.update(book).set(book.bookViewCount, book.bookViewCount.add(1))
                .where(book.bookId.eq(bookId)).execute();
    }

    /**
     * 도서 Id로 도서를 검색합니다.
     * @param bookId 도서 ID
     * @return 도서 기본 정보
     */
    @Override
    public BookBaseResponse findBookById(Long bookId) {
        addViewCount(book, bookId);
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
                .where(book.bookId.eq(bookId))
                .fetchOne();
    }

    /**
     * 책 아이디로 책의 카테고리들을 가져옵니다.
     * @param bookId 책 아이디
     * @return 책의 카테고리 리스트
     */
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

    /**
     * 책 아이디로 책의 태그들을 가져옵니다.
     * @param bookId 책 아이디
     * @return 책의 태그 리스트
     */
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

    /**
     * 책 아이디로 책의 작가들을 가져옵니다.
     * @param bookId 책 아이디
     * @return 책의 작가 리스트
     */
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


    /**
     * 책 ID로 책의 작가(이름)를 가져옵니다.
     * @param bookId 책 아이디
     * @return 작가 이름
     */
    @Override
    public List<String> findAuthorNamesByBookId(Long bookId) {
        QBook book = QBook.book;
        QAuthor author = QAuthor.author;
        QBookAuthor bookAuthor = QBookAuthor.bookAuthor;

        return queryFactory
            .select(author.authorName)
            .from(book)
            .join(bookAuthor).on(book.bookId.eq(bookAuthor.book.bookId))
            .join(author).on(author.authorId.eq(bookAuthor.author.authorId))
            .where(book.bookId.eq(bookId))
            .fetch();
    }


    /**
     * 책으로 출판사명을 가져옴.
     * @param bookId
     * @return 출판사명
     */
    @Override
    public String findPublisherNameByBookId(Long bookId) {
        QBook book = QBook.book;
        return queryFactory
            .select(book.publisher.publisherName)
            .from(book)
            .where(book.bookId.eq(bookId))
            .fetchOne();
    }


    /**
     * 책으로 태그들의 name을 조회
     * @param bookId
     * @return 태그명 반환 (없다면 null을 그대로 저장)
     */
    @Override
    public List<String> findTagNamesByBookId(Long bookId){
        QBookTag qBookTag = QBookTag.bookTag;
        QTag tag = QTag.tag;

        return queryFactory
            .select(tag.tagName)
            .from(bookTag)
            .join(bookTag.tag, tag)
            .where(bookTag.book.bookId.eq(bookId))
            .fetch();
    }


    /**
     * 책으로 카테고리들의 name을 조회
     * @param bookId
     * @return
     */
    @Override
    public List<String> findCategoryNamesByBookId(Long bookId){
        QBookCategory bookCategory = QBookCategory.bookCategory;
        QCategory category = QCategory.category;

        return queryFactory
            .select(category.categoryName)
            .from(bookCategory)
            .join(bookCategory.category, category)
            .where(bookCategory.book.bookId.eq(bookId))
            .fetch();
    }

    //todo: 현재 구현되어 있는 카테고리, 태그, 작가 리스트를 불러오는 방식이 N+1 문제를 발생시킬 것 같아서
    // 다른 방식 구현 중
    @Override
    public List<BookCategoryDto> findCategoriesByBookIds(List<Long> bookIds) {
        return queryFactory
                .select(Projections.constructor(BookCategoryDto.class,
                        bookCategory.book.bookId,
                        Projections.constructor(CategoryNameResponse.class,
                                category.categoryId,
                                category.categoryName)))
                .from(bookCategory)
                .leftJoin(bookCategory.category, category)
                .where(bookCategory.book.bookId.in(bookIds))
                .fetch();
    }

    @Override
    public List<BookTagDto> findTagsByBookIds(List<Long> bookIds) {
        return queryFactory
                .select(Projections.constructor(BookTagDto.class,
                        bookTag.book.bookId,
                        Projections.constructor(TagInfoResponse.class,
                                tag.tagId,
                                tag.tagName)))
                .from(bookTag)
                .leftJoin(bookTag.tag, tag)
                .where(bookTag.book.bookId.in(bookIds))
                .fetch();
    }

    @Override
    public List<BookAuthorDto> findAuthorsByBookIds(List<Long> bookIds) {
        return queryFactory
                .select(Projections.constructor(BookAuthorDto.class,
                        bookAuthor.book.bookId,
                        Projections.constructor(AuthorNameResponse.class,
                                author.authorId,
                                author.authorName)))
                .from(bookAuthor)
                .leftJoin(bookAuthor.author, author)
                .where(bookAuthor.book.bookId.in(bookIds))
                .fetch();
    }
}

