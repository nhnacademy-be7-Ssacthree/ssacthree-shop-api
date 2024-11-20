package com.nhnacademy.ssacthree_shop_api.review.service;

import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ReviewService {

    List<ReviewResponse> getReviewsByBookId(Long bookId);

    ResponseEntity<Void> postReviewBook(String header, Long bookId,Long orderId, ReviewRequestWithUrl reviewRequest);

    Long authToWriteReview(String header, Long bookId);

}
