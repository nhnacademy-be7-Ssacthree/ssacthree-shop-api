package com.nhnacademy.ssacthree_shop_api.review.controller;

import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import com.nhnacademy.ssacthree_shop_api.review.dto.MemberReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/books/reviews/{book-id}")
    public ResponseEntity<Page<BookReviewResponse>> getReviewsByBookId(@PathVariable("book-id") Long bookId,
                                                                       @RequestParam int page,
                                                                       @RequestParam int size,
                                                                       @RequestParam("sort") String[] sort) {
        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);
        Page<BookReviewResponse> reviews = reviewService.getReviewsByBookId(pageable, bookId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/members/reviews")
    public ResponseEntity<Void> postReviewBook(
            @RequestHeader(name = "X-USER-ID") String header,
            @RequestParam("book-id") Long bookId,
            @RequestParam("order-id") Long orderId,
            @RequestBody ReviewRequestWithUrl reviewRequest) {
        return reviewService.postReviewBook(header,bookId,orderId,reviewRequest);
    }
    @GetMapping("/members/reviews/{book-id}")
    public ResponseEntity<Long> authToWriteReview(
            @RequestHeader(name = "X-USER-ID") String header,
            @PathVariable("book-id") Long bookId) {
        Long orderId = reviewService.authToWriteReview(header,bookId);
        if(orderId != null) {
            return new ResponseEntity<>(orderId,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
    }
    @GetMapping("/members/reviews")
    public ResponseEntity<List<MemberReviewResponse>> getReviewsByMemberId(
            @RequestHeader(name = "X-USER-ID") String header){
        List<MemberReviewResponse> reviews = reviewService.getReviewsByMemberId(header);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
    @GetMapping("/members/reviews/update/{order-id}/{book-id}")
    public ResponseEntity<ReviewResponse> getReview(
            @RequestHeader(name = "X-USER-ID") String header,
            @PathVariable("order-id") Long orderId,
            @PathVariable("book-id") Long bookId) {
        ReviewResponse reviewResponse = reviewService.getReview(header,orderId,bookId);
        return new ResponseEntity<>(reviewResponse,HttpStatus.OK);
    }
}