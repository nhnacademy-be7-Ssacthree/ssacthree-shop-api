package com.nhnacademy.ssacthree_shop_api.review.service;

import com.nhnacademy.ssacthree_shop_api.review.dto.MemberReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReviewService {

    Page<BookReviewResponse> getReviewsByBookId(Pageable pageable, Long bookId);
    ResponseEntity<Void> postReviewBook(String header, Long bookId,Long orderId, ReviewRequestWithUrl reviewRequest);

    Long authToWriteReview(String header, Long bookId);

    List<MemberReviewResponse> getReviewsByMemberId(String header);

    ReviewResponse getReview(String header, Long orderId, Long bookId);
}
