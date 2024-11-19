package com.nhnacademy.ssacthree_shop_api.review.service;

import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getReviewsByBookId(Long bookId);

}
