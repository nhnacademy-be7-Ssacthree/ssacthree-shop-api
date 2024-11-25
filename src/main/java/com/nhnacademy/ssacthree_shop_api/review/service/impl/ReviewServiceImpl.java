package com.nhnacademy.ssacthree_shop_api.review.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.order.domain.Order;
import com.nhnacademy.ssacthree_shop_api.orderset.order.exception.NotFoundOrderException;
import com.nhnacademy.ssacthree_shop_api.orderset.order.repository.OrderRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.domain.OrderDetail;
import com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.repo.OrderDetailRepository;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import com.nhnacademy.ssacthree_shop_api.review.domain.ReviewId;
import com.nhnacademy.ssacthree_shop_api.review.dto.MemberReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.exception.ReviewNotFoundException;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewRepository;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository; //리뷰 찾기
    private final BookRepository bookRepository; //책 아이디로 책 찾기
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookReviewResponse> getReviewsByBookId(Long bookId) {
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("해당 책을 찾을 수 없습니다."));
        List<Review> reviews = reviewRepository.findAllByBook(book);
        List<BookReviewResponse> bookReviewRespons = new ArrayList<>();

        for (Review review : reviews) {
            Member member = memberRepository.findById(review.getCustomer().getCustomerId()).orElseThrow(
                () -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
            BookReviewResponse bookReviewResponse = new BookReviewResponse(member.getMemberLoginId(),review.getReviewRate(),review.getReviewTitle(),review.getReviewContent(),review.getReviewCreatedAt(),review.getReviewImageUrl());
            bookReviewRespons.add(bookReviewResponse);
        }

        return bookReviewRespons;
    }

    @Override //리뷰 작성 저장
    public ResponseEntity<Void> postReviewBook(String header, Long bookId, Long orderId,
        ReviewRequestWithUrl reviewRequest) {

        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundOrderException("order not found"));
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("book not found"));

        ReviewId reviewId = new ReviewId(orderId,bookId);

        Review review = new Review(reviewId,order,book,member.getCustomer(),reviewRequest.getReviewRate(),reviewRequest.getReviewTitle(),reviewRequest.getReviewContent(),reviewRequest.getReviewImageUrl()); //일단 사진 저장 보류

        reviewRepository.save(review);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override //리뷰를 쓸 권한이 있는지 확인
    public Long authToWriteReview(String header, Long bookId) {

        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));
        List<Order> orderList = orderRepository.findOrderByCustomer(member.getCustomer());
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("book not found"));
        for (Order order : orderList) {
            Optional<OrderDetail> auth = orderDetailRepository.findAllByBookAndOrder(book, order);
            Optional<Review> review = reviewRepository.findReviewByBookAndOrder(book,order);
            if(auth.isPresent() && review.isEmpty()) {
                return order.getId();
            }
        }

        return null;
    }

    @Override
    public List<MemberReviewResponse> getReviewsByMemberId(String header) {
        Member member = memberRepository.findByMemberLoginId(header).orElseThrow(() -> new MemberNotFoundException("member not found"));

        List<Review> reviews = reviewRepository.findAllByCustomer(member.getCustomer());
        List<MemberReviewResponse> reviewResponses = new ArrayList<>();

        for (Review review : reviews) {
            reviewResponses.add(new MemberReviewResponse(review.getReviewId().getOrderId(),review.getReviewId().getBookId(),review.getReviewRate(),review.getReviewTitle(),review.getReviewContent(),review.getReviewImageUrl()));
        }

        return reviewResponses;
    }

    @Override
    public ReviewResponse getReview(String header, Long orderId, Long bookId) {
        ReviewId reviewId = new ReviewId(orderId,bookId);
        Review review = reviewRepository.findByReviewId(reviewId).orElseThrow(() -> new ReviewNotFoundException("review not found"));

        return new ReviewResponse(review.getReviewRate(),review.getReviewTitle(),review.getReviewContent(),review.getReviewImageUrl());
    }
}
