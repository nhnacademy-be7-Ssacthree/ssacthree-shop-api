package com.nhnacademy.ssacthree_shop_api.review.repository;

import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {

    Page<BookReviewResponse> findReviewsByBookId(Long bookId, Pageable pageable);

}
