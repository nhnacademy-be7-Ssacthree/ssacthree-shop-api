package com.nhnacademy.ssacthree_shop_api.review.repo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.nhnacademy.ssacthree_shop_api.customer.domain.QCustomer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.QMember;
import com.nhnacademy.ssacthree_shop_api.review.domain.QReview;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.repository.impl.ReviewCustomRepositoryImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class ReviewCustomRepositoryImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private ReviewCustomRepositoryImpl reviewCustomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findReviewsByBookId() {
        // Given
        Long bookId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);

        // Mock 데이터 생성
        BookReviewResponse mockResponse = new BookReviewResponse(
            "user123", // memberLoginId
            5,         // reviewRate
            "Great Book", // reviewTitle
            "Amazing read!", // reviewContent
            LocalDateTime.now(), // reviewCreatedAt
            "http://example.com/image.jpg" // reviewImageUrl
        );
        List<BookReviewResponse> mockResult = Collections.singletonList(mockResponse);

        JPAQuery mockQuery = mock(JPAQuery.class);

        // Mock queryFactory select call
        when(queryFactory.select(
            Projections.constructor(BookReviewResponse.class,
                QMember.member.memberLoginId,
                QReview.review.reviewRate,
                QReview.review.reviewTitle,
                QReview.review.reviewContent,
                QReview.review.reviewCreatedAt,
                QReview.review.reviewImageUrl)))
            .thenReturn(mockQuery);

        // Mock from, join, on, where, and orderBy calls
        when(mockQuery.from(QReview.review)).thenReturn(mockQuery);
        when(mockQuery.join(QReview.review.customer, QCustomer.customer)).thenReturn(mockQuery);
        when(mockQuery.join(QMember.member)).thenReturn(mockQuery);
        when(mockQuery.on(QMember.member.customer.customerId.eq(QCustomer.customer.customerId)))
            .thenReturn(mockQuery);
        when(mockQuery.where(QReview.review.book.bookId.eq(bookId))).thenReturn(mockQuery);
        when(mockQuery.orderBy(QReview.review.reviewCreatedAt.desc())).thenReturn(mockQuery);

        // Mock offset and limit calls
        when(mockQuery.offset(pageable.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageable.getPageSize())).thenReturn(mockQuery);

        // 첫 번째 fetch()는 총 갯수를 반환하도록 설정
        when(mockQuery.fetch()).thenReturn(mockResult).thenReturn(mockResult);

        // When
        Page<BookReviewResponse> result = reviewCustomRepository.findReviewsByBookId(bookId, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getMemberId()).isEqualTo("user123");
        assertThat(result.getContent().getFirst().getReviewRate()).isEqualTo(5);

        // Verify interactions
        verify(queryFactory).select(
            Projections.constructor(BookReviewResponse.class,
                QMember.member.memberLoginId,
                QReview.review.reviewRate,
                QReview.review.reviewTitle,
                QReview.review.reviewContent,
                QReview.review.reviewCreatedAt,
                QReview.review.reviewImageUrl));
        verify(mockQuery).from(QReview.review);
        verify(mockQuery).join(QReview.review.customer, QCustomer.customer);
        verify(mockQuery).join(QMember.member);
        verify(mockQuery).on(QMember.member.customer.customerId.eq(QCustomer.customer.customerId));
        verify(mockQuery).where(QReview.review.book.bookId.eq(bookId));
        verify(mockQuery).orderBy(QReview.review.reviewCreatedAt.desc());
        verify(mockQuery).offset(pageable.getOffset());
        verify(mockQuery).limit(pageable.getPageSize());
        verify(mockQuery, times(2)).fetch(); // fetch() 호출이 2번인지 검증
    }
}