package com.nhnacademy.ssacthree_shop_api.review.repository.impl;

import com.nhnacademy.ssacthree_shop_api.customer.domain.QCustomer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.QMember;
import com.nhnacademy.ssacthree_shop_api.review.domain.QReview;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookReviewResponse> findReviewsByBookId(Long bookId, Pageable pageable) {
        QReview review = QReview.review;
        QCustomer customer = QCustomer.customer;
        QMember member = QMember.member;

        // QueryDSL로 리뷰 조회 쿼리 작성
        JPAQuery<BookReviewResponse> query = queryFactory
                .select(Projections.constructor(BookReviewResponse.class,
                        member.memberLoginId,       // Member Login ID
                        review.reviewRate,          // 리뷰 평점
                        review.reviewTitle,         // 리뷰 제목
                        review.reviewContent,       // 리뷰 내용
                        review.reviewCreatedAt,     // 리뷰 생성일
                        review.reviewImageUrl       // 리뷰 이미지 URL
                ))
                .from(review)
                .join(review.customer, customer) // Review -> Customer 조인
                .join(member).on(member.customer.customerId.eq(customer.customerId)) // Customer -> Member 조인
                .where(review.book.bookId.eq(bookId)) // bookId 조건
                .orderBy(review.reviewCreatedAt.desc()); // 최신순 정렬

        // 페이징 처리
        long total = query.fetch().size();
        List<BookReviewResponse> reviews = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체로 반환
        return new PageImpl<>(reviews, pageable, total);
    }
}
