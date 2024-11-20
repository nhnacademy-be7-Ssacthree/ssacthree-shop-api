package com.nhnacademy.ssacthree_shop_api.review.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.customer.repository.CustomerRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.exception.MemberNotFoundException;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberRepository;
import com.nhnacademy.ssacthree_shop_api.review.domain.Review;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.repository.ReviewRepository;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository; //리뷰 찾기
    private final BookRepository bookRepository; //책 아이디로 책 찾기
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByBookId(Long bookId) {
        Book book = bookRepository.findByBookId(bookId).orElseThrow(() -> new BookNotFoundException("해당 책을 찾을 수 없습니다."));
        List<Review> reviews = reviewRepository.findAllByBook(book);
        List<ReviewResponse> reviewResponses = new ArrayList<>();

        for (Review review : reviews) {
            Member member = memberRepository.findById(review.getCustomer().getCustomerId()).orElseThrow(
                () -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
            ReviewResponse reviewResponse = new ReviewResponse(member.getMemberLoginId(),review.getReviewRate(),review.getReviewTitle(),review.getReviewContent(),review.getReviewCreatedAt(),review.getReviewImageUrl());
            reviewResponses.add(reviewResponse);
        }

        return reviewResponses;
    }
}
