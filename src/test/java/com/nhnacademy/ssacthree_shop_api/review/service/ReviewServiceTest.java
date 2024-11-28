package com.nhnacademy.ssacthree_shop_api.review.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.repository.PointHistoryRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveRule;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.domain.PointSaveType;
import com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.repository.PointSaveRuleRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import com.nhnacademy.ssacthree_shop_api.review.domain.ReviewId;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.MemberReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewCustomRepository;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewRepository;
import com.nhnacademy.ssacthree_shop_api.review.service.impl.ReviewServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Mock
    private PointSaveRuleRepository pointSaveRuleRepository;

    @Mock
    private ReviewCustomRepository reviewCustomRepository;

    @Test
    void testGetReviewsByBookId_Success() {
        // Given
        Long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookReviewResponse> mockReviews = new PageImpl<>(List.of(
            new BookReviewResponse("User123", 5, "Excellent Book", "Loved it!", LocalDateTime.now(), "image_url")
        ));

        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(new Book()));
        when(reviewCustomRepository.findReviewsByBookId(bookId, pageable)).thenReturn(mockReviews);

        // When
        Page<BookReviewResponse> result = reviewService.getReviewsByBookId(pageable, bookId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(reviewCustomRepository, times(1)).findReviewsByBookId(bookId, pageable);
    }

    @Test
    void testGetReviewsByBookId_BookNotFound() {
        // Given
        Long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
            BookNotFoundException.class, () -> reviewService.getReviewsByBookId(pageable, bookId));
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(reviewCustomRepository, never()).findReviewsByBookId(anyLong(), any(Pageable.class));
    }

    @Test
    void testPostReviewBook_Success() {
        // Given
        String header = "testUser";
        Long bookId = 1L;
        Long orderId = 1L;
        ReviewRequestWithUrl reviewRequest = new ReviewRequestWithUrl(5, "Great Book", "Loved it!", "");

        Member member = new Member();
        member.setMemberPoint(0);
        Book book = new Book();
        Order order = new Order();

        when(memberRepository.findByMemberLoginId(header)).thenReturn(Optional.of(member));
        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(book));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(reviewRepository.findByReviewId(any(ReviewId.class))).thenReturn(Optional.empty());
        when(pointSaveRuleRepository.findPointSaveRuleByPointSaveRuleName("리뷰작성사진없음"))
            .thenReturn(Optional.of(new PointSaveRule("리뷰작성사진없음", 10, PointSaveType.INTEGER)));

        // When
        ResponseEntity<Void> response = reviewService.postReviewBook(header, bookId, orderId, reviewRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(memberRepository, times(1)).findByMemberLoginId(header);
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    void testAuthToWriteReview_Success() {
        // Given
        String header = "testUser";
        Long bookId = 1L;

        Member member = new Member();
        Book book = new Book();
        Order order = new Order();

        when(memberRepository.findByMemberLoginId(header)).thenReturn(Optional.of(member));
        when(bookRepository.findByBookId(bookId)).thenReturn(Optional.of(book));
        when(orderRepository.findOrderByCustomer(any())).thenReturn(List.of(order));
        when(orderDetailRepository.findAllByBookAndOrder(book, order)).thenReturn(Optional.of(new OrderDetail()));
        when(reviewRepository.findReviewByBookAndOrder(book, order)).thenReturn(Optional.empty());

        // When
        Long result = reviewService.authToWriteReview(header, bookId);

        // Then
        assertEquals(order.getId(), result);
        verify(memberRepository, times(1)).findByMemberLoginId(header);
        verify(bookRepository, times(1)).findByBookId(bookId);
        verify(orderRepository, times(1)).findOrderByCustomer(any());
        verify(orderDetailRepository, times(1)).findAllByBookAndOrder(book, order);
    }

    @Test
    void testGetReviewsByMemberId_Success() {
        // Given
        String header = "testUser";
        Member member = new Member();
        member.setCustomer(new Customer());
        List<Review> reviews = List.of(
            new Review(new ReviewId(1L, 1L), new Order(), new Book(), member.getCustomer(), 5, "Great Book", "Loved it!", "image_url")
        );

        when(memberRepository.findByMemberLoginId(header)).thenReturn(Optional.of(member));
        when(reviewRepository.findAllByCustomer(member.getCustomer())).thenReturn(reviews);

        // When
        List<MemberReviewResponse> result = reviewService.getReviewsByMemberId(header);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Great Book", result.get(0).getReviewTitle());
        verify(memberRepository, times(1)).findByMemberLoginId(header);
        verify(reviewRepository, times(1)).findAllByCustomer(member.getCustomer());
    }

    @Test
    void testGetReview_Success() {
        // Given
        String header = "testUser";
        Long orderId = 1L;
        Long bookId = 1L;
        Review review = new Review(new ReviewId(orderId, bookId), new Order(), new Book(), new Customer(), 5, "Great Book", "Loved it!", "image_url");

        when(reviewRepository.findByReviewId(new ReviewId(orderId, bookId))).thenReturn(Optional.of(review));

        // When
        ReviewResponse result = reviewService.getReview(header, orderId, bookId);

        // Then
        assertNotNull(result);
        assertEquals("Great Book", result.getReviewTitle());
        verify(reviewRepository, times(1)).findByReviewId(any(ReviewId.class));
    }
}