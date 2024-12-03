package com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.QAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.QBook;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookCustomRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.QBookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.QBookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.domain.QBookLike;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.QBookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.QPublisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.QTag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;
import com.nhnacademy.ssacthree_shop_api.commons.util.QueryDslSortUtil;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.QMember;
import com.nhnacademy.ssacthree_shop_api.review.domain.QReview;
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
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.types.Projections.list;

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
    private static final QBookLike bookLike = QBookLike.bookLike;
    private static final QMember member = QMember.member;
    private static final QReview review = QReview.review;

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
    @SuppressWarnings("squid:S3740")
    private interface JoinClause {
        void apply(JPAQuery<?> query);
    }

    /**
     * 각 도서의 좋아요 수를 가져옵니다.
     * @param bookIds 도서 아이디 리스트
     * @return 도서아이디 마다의 좋아요 수를 map 형태로 저장함
     */
    private Map<Long, Long> fetchLikeCountsForBooks(List<Long> bookIds) {
        return queryFactory
                .select(bookLike.book.bookId, bookLike.count())
                .from(bookLike)
                .where(bookLike.book.bookId.in(bookIds))
                .groupBy(bookLike.book.bookId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(bookLike.book.bookId),
                        tuple -> {
                            Long count = tuple.get(bookLike.count());
                            return count != null ? count : 0L;
                        }
                ));
    }

    /**
     * 각 도서의 리뷰 수를 가져옵니다
     * @param bookIds 도서 아이디 리스트
     * @return 도서 아이디 마다의 리뷰 수를 map 형태로 저장함
     */
    private Map<Long, Long> fetchReviewCountsForBooks(List<Long> bookIds) {
        return queryFactory
                .select(review.book.bookId, review.count())
                .from(review)
                .where(review.book.bookId.in(bookIds))
                .groupBy(review.book.bookId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(review.book.bookId),
                        tuple -> {
                            Long count = tuple.get(review.count());
                            return count != null ? count : 0L;
                        }
                ));
    }

    /**
     * 각 도서의 리뷰 평점 점수를 가져옵니다.
     * @param bookIds 도서 아이디 리스트
     * @return 도서 아이디 마다의 리뷰 평점 점수를 가져옵니다.
     */
    private Map<Long, Double> fetchReviewRateAveragesForBooks(List<Long> bookIds) {
        return queryFactory
                .select(review.book.bookId, review.reviewRate.avg())
                .from(review)
                .where(review.book.bookId.in(bookIds))
                .groupBy(review.book.bookId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(review.book.bookId),
                        tuple -> {
                            Double avg = tuple.get(review.reviewRate.avg());
                            return avg != null ? avg : 0.0;
                        }
                ));
    }

    private Map<Long, List<CategoryNameResponse>> fetchCategoriesByBookIds(List<Long> bookIds) {
        return queryFactory
                .select(bookCategory.book.bookId, category.categoryId, category.categoryName)
                .from(bookCategory)
                .leftJoin(bookCategory.category, category)
                .where(bookCategory.book.bookId.in(bookIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(bookCategory.book.bookId),
                        Collectors.mapping(
                                tuple -> new CategoryNameResponse(
                                        tuple.get(category.categoryId),
                                        tuple.get(category.categoryName)
                                ),
                                Collectors.toList()
                        )
                ));
    }

    private Map<Long, List<TagInfoResponse>> fetchTagsByBookIds(List<Long> bookIds) {
        return queryFactory
                .select(bookTag.book.bookId, tag.tagId, tag.tagName)
                .from(bookTag)
                .leftJoin(bookTag.tag, tag)
                .where(bookTag.book.bookId.in(bookIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(bookTag.book.bookId),
                        Collectors.mapping(
                                tuple -> new TagInfoResponse(
                                        tuple.get(tag.tagId),
                                        tuple.get(tag.tagName)
                                ),
                                Collectors.toList()
                        )
                ));
    }

    private Map<Long, List<AuthorNameResponse>> fetchAuthorsByBookIds(List<Long> bookIds) {
        return queryFactory
                .select(bookAuthor.book.bookId, author.authorId, author.authorName)
                .from(bookAuthor)
                .leftJoin(bookAuthor.author, author)
                .where(bookAuthor.book.bookId.in(bookIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(bookAuthor.book.bookId),
                        Collectors.mapping(
                                tuple -> new AuthorNameResponse(
                                        tuple.get(author.authorId),
                                        tuple.get(author.authorName)
                                ),
                                Collectors.toList()
                        )
                ));
    }


    /**
     * 공통 메소드 처리
     * @param pageable 페이징 처리
     * @param condition where 조건절
     * @param joinConditions 조인 리스트
     * @return 도서 기본 정보 페이지
     */
    @SuppressWarnings("squid:S3740")
    private Page<BookListResponse> findBooksByCondition(
            Pageable pageable,
            Predicate condition,
            List<JoinClause> joinConditions
    ) {

        JPAQuery<BookListBaseResponse> query = queryFactory
                .select(Projections.constructor(BookListBaseResponse.class,
                        book.bookId,
                        book.bookName,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
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
        PathBuilder<Book> pathBuilder = new PathBuilder<>(QBook.book.getType(), QBook.book.getMetadata());
        QueryDslSortUtil.applyOrderBy(query, pageable.getSort(), pathBuilder);

        // 페이징 처리 및 결과 조회
        List<BookListBaseResponse> books = query
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

        // 책 ID 목록 추출
        List<Long> bookIds = books.stream()
                .map(BookListBaseResponse::getBookId)
                .toList();

        // 4. 연관 데이터 한 번에 가져오기
        Map<Long, List<CategoryNameResponse>> categoriesMap = fetchCategoriesByBookIds(bookIds);
        Map<Long, List<TagInfoResponse>> tagsMap = fetchTagsByBookIds(bookIds);
        Map<Long, List<AuthorNameResponse>> authorsMap = fetchAuthorsByBookIds(bookIds);

        // 집계 데이터 가져오기
        Map<Long, Long> likeCounts = fetchLikeCountsForBooks(bookIds);
        Map<Long, Long> reviewCounts = fetchReviewCountsForBooks(bookIds);
        Map<Long, Double> reviewRateAverages = fetchReviewRateAveragesForBooks(bookIds);

        List<BookListResponse> content = books.stream()
                .map(b ->{
                    BookListResponse bookListResponse= new BookListResponse(b);
                    bookListResponse.setCategories(categoriesMap.get(b.getBookId()));
                    bookListResponse.setTags(tagsMap.get(b.getBookId()));
                    bookListResponse.setAuthors(authorsMap.get(b.getBookId()));
                    bookListResponse.setLikeCount(likeCounts.get(b.getBookId()));
                    bookListResponse.setReviewCount(reviewCounts.get(b.getBookId()));
                    bookListResponse.setReviewRateAverage(reviewRateAverages.get(b.getBookId()));
                    return bookListResponse;
                }).toList();


        return new PageImpl<>(content, pageable, count);
    }


    /**
     * 책 이름을 포함하고 있는 책을 검색합니다.
     * @param pageable 페이징 처리
     * @param bookName 찾고자 하는 책 이름
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookListResponse> findBooksByBookName(Pageable pageable, String bookName) {
        Predicate condition = isOnSaleOrNoStock().and(book.bookName.containsIgnoreCase(bookName));
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 판매 중, 재고 없는 상태의 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    public Page<BookListResponse> findAllAvailableBooks(Pageable pageable) {
        Predicate condition = isOnSaleOrNoStock();
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 재고가 없는 모든 책을 조회합니다.
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookListResponse> findAllBooksByStatusNoStock(Pageable pageable) {
        Predicate condition = book.bookStatus.eq(BookStatus.NO_STOCK);
        return findBooksByCondition(pageable, condition, List.of());
    }

    /**
     * 판매 중단된 책을 모두 검색합니다.
     * @param pageable 페이징 처리
     * @return Page<BookBaseResponse>
     */
    @Override
    public Page<BookListResponse> findStatusDiscontinued(Pageable pageable) {
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
    public Page<BookListResponse> findBooksByAuthorId(Long authorId, Pageable pageable) {
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
    public Page<BookListResponse> findBooksByTagId(Long tagId, Pageable pageable) {
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
    public Page<BookListResponse> findBooksByCategoryId(Long categoryId, Pageable pageable) {
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
     * 회원의 좋아요 도서 정보 목록 검색
     * @param customerId 회원 아이디
     * @param pageable 페이징 처리
     * @return 도서 기본 정보
     */
    @SuppressWarnings("squid:S3740")
    @Override
    public Page<BookListResponse> findBookLikesByCustomerId(Long customerId, Pageable pageable) {

        JPAQuery<BookListBaseResponse> query = queryFactory
                .select(Projections.constructor(BookListBaseResponse.class,
                        book.bookId,
                        book.bookName,
                        book.publicationDate,
                        book.regularPrice,
                        book.salePrice,
                        book.bookThumbnailImageUrl,
                        book.bookViewCount,
                        book.bookDiscount,
                        book.bookStatus.stringValue(),
                        Projections.constructor(PublisherNameResponse.class,
                                publisher.publisherId,
                                publisher.publisherName)
                ))
                .from(bookLike)
                .leftJoin(bookLike.book, book)
                .leftJoin(bookLike.member, member)
                .leftJoin(book.publisher, publisher)
                .where(member.id.eq(customerId)
                        .and(isOnSaleOrNoStock()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 정렬 추가
        PathBuilder<Book> pathBuilder = new PathBuilder<>(QBook.book.getType(), QBook.book.getMetadata());
        QueryDslSortUtil.applyOrderBy(query, pageable.getSort(), pathBuilder);

        // 데이터 조회
        List<BookListBaseResponse> books = query.fetch();

        Long totalCount = queryFactory
                .select(bookLike.count())
                .from(bookLike)
                .leftJoin(bookLike.book, book)
                .leftJoin(bookLike.member, member)
                .where(member.id.eq(customerId)
                        .and(isOnSaleOrNoStock()))
                .fetchOne();

        totalCount = totalCount != null ? totalCount : 0L; // Null 방지

        // 책 ID 목록 추출
        List<Long> bookIds = books.stream()
                .map(BookListBaseResponse::getBookId)
                .toList();

        // 4. 연관 데이터 한 번에 가져오기
        Map<Long, List<CategoryNameResponse>> categoriesMap = fetchCategoriesByBookIds(bookIds);
        Map<Long, List<TagInfoResponse>> tagsMap = fetchTagsByBookIds(bookIds);
        Map<Long, List<AuthorNameResponse>> authorsMap = fetchAuthorsByBookIds(bookIds);

        // 집계 데이터 가져오기
        Map<Long, Long> likeCounts = fetchLikeCountsForBooks(bookIds);
        Map<Long, Long> reviewCounts = fetchReviewCountsForBooks(bookIds);
        Map<Long, Double> reviewRateAverages = fetchReviewRateAveragesForBooks(bookIds);

        List<BookListResponse> content = books.stream()
                .map(b ->{
                    BookListResponse bookListResponse= new BookListResponse(b);
                    bookListResponse.setCategories(categoriesMap.get(b.getBookId()));
                    bookListResponse.setTags(tagsMap.get(b.getBookId()));
                    bookListResponse.setAuthors(authorsMap.get(b.getBookId()));
                    bookListResponse.setLikeCount(likeCounts.get(b.getBookId()));
                    bookListResponse.setReviewCount(reviewCounts.get(b.getBookId()));
                    bookListResponse.setReviewRateAverage(reviewRateAverages.get(b.getBookId()));
                    return bookListResponse;
                }).toList();

        return new PageImpl<>(content, pageable, totalCount);
    }


    /**
     * 회원의 좋아요 도서 아이디 리스트
     * @param customerId 회원 아이디
     * @return 도서 아이디
     */
    @Override
    public List<Long> findLikedBookIdByCustomerId(Long customerId) {
        return queryFactory.select(bookLike.book.bookId)
                .from(bookLike).where(bookLike.member.id.eq(customerId)).fetch();
    }

    /**
     * 특정 책의 좋아요 수
     * @param bookId 도서 아이디
     * @return 좋아요 수
     */
    @Override
    public Long findBookLikeByBookId(Long bookId) {
        return queryFactory.select(bookLike.count()).from(bookLike)
                .where(bookLike.book.bookId.eq(bookId)).fetchOne();
    }

    /**
     * 특정 책의 리뷰 수
     * @param bookId 도서 아이디
     * @return 특정 책의 리뷰 수
     */
    @Override
    public Long findReviewCountByBookId(Long bookId) {
        return queryFactory.select(review.count()).from(review)
                .where(review.book.bookId.eq(bookId)).fetchOne();
    }

    /**
     * 특정 책의 리뷰 별점 평균
     * @param bookId 도서 아이디
     * @return 특정 책의 리뷰 별점 평균
     */
    @Override
    public Double findReviewRateAverageByBookId(Long bookId) {
        Double average = queryFactory.select(review.reviewRate.avg())
                .from(review)
                .where(review.book.bookId.eq(bookId))
                .fetchOne();

        return average != null ? average : 0.0; // 리뷰가 없으면 0.0 반환
    }

    /**
     * isbn으로 도서를 검색합니다.
     * @param isbn 도서 isbn
     * @return 도서 기본 정보
     */
    @Override
    public BookInfoResponse findByBookIsbn(String isbn) {
        BookBaseResponse base = queryFactory.select(Projections.constructor(BookBaseResponse.class,
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

        if(base == null) {
            throw new NotFoundException("도서를 찾을 수 없습니다.");
        }

        List<Long> bookIds = List.of(base.getBookId());

        // 4. 연관 데이터 한 번에 가져오기
        Map<Long, List<CategoryNameResponse>> categoriesMap = fetchCategoriesByBookIds(bookIds);
        Map<Long, List<TagInfoResponse>> tagsMap = fetchTagsByBookIds(bookIds);
        Map<Long, List<AuthorNameResponse>> authorsMap = fetchAuthorsByBookIds(bookIds);


        BookInfoResponse content = new BookInfoResponse(base);
        content.setCategories(categoriesMap.get(base.getBookId()));
        content.setTags(tagsMap.get(base.getBookId()));
        content.setAuthors(authorsMap.get(base.getBookId()));

        return content;
    }

    private synchronized void addViewCount(QBook qBook, Long bookId){
        queryFactory.update(qBook).set(qBook.bookViewCount, qBook.bookViewCount.add(1))
                .where(qBook.bookId.eq(bookId)).execute();
    }

    /**
     * 도서 Id로 도서를 검색합니다.
     * @param bookId 도서 ID
     * @return 도서 기본 정보
     */
    @Override
    public BookInfoResponse findBookById(Long bookId) {
        addViewCount(book, bookId); // 조회수 증가
        BookBaseResponse base =  queryFactory.select(Projections.constructor(BookBaseResponse.class,
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

        if(base==null) {
            throw new NotFoundException("도서를 찾을 수 없습니다.");
        }

        List<Long> bookIds = List.of(base.getBookId());

        // 4. 연관 데이터 한 번에 가져오기
        Map<Long, List<CategoryNameResponse>> categoriesMap = fetchCategoriesByBookIds(bookIds);
        Map<Long, List<TagInfoResponse>> tagsMap = fetchTagsByBookIds(bookIds);
        Map<Long, List<AuthorNameResponse>> authorsMap = fetchAuthorsByBookIds(bookIds);


        BookInfoResponse content = new BookInfoResponse(base);
        content.setCategories(categoriesMap.get(base.getBookId()));
        content.setTags(tagsMap.get(base.getBookId()));
        content.setAuthors(authorsMap.get(base.getBookId()));

        return content;
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


        return queryFactory
            .select(category.categoryName)
            .from(bookCategory)
            .join(bookCategory.category, category)
            .where(bookCategory.book.bookId.eq(bookId))
            .fetch();
    }


}

