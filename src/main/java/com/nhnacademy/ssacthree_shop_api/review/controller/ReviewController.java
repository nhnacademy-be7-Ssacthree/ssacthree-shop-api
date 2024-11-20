package com.nhnacademy.ssacthree_shop_api.review.controller;

import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("books/reviews/{book-id}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByBookId(@PathVariable("book-id") Long bookId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("members/reviews")
    public ResponseEntity<Void> postReviewBook(
        @RequestHeader(name = "X-USER-ID") String header,
        @RequestParam("book-id") Long bookId,
        @RequestParam("order-id") Long orderId,
        @RequestBody ReviewRequestWithUrl reviewRequest) {
        ResponseEntity<Void> responseEntity = reviewService.postReviewBook(header,bookId,orderId,reviewRequest);

        return responseEntity;
    }

    @GetMapping("members/reviews/{book-id}")
    public ResponseEntity<Long> authToWriteReview(
        @RequestHeader(name = "X-USER-ID") String header,
        @PathVariable("book-id") Long bookId) {

        Long orderId = reviewService.authToWriteReview(header,bookId);
        if(orderId != null) {
            return new ResponseEntity<>(orderId,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }
}
