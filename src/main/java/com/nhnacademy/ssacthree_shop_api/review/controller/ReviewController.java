package com.nhnacademy.ssacthree_shop_api.review.controller;

import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/books/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("{book-id}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByBookId(@PathVariable("book-id") Long bookId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
