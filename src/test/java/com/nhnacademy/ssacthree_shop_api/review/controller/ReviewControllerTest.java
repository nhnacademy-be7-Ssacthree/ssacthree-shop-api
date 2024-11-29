package com.nhnacademy.ssacthree_shop_api.review.controller;

import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.review.dto.MemberReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.ssacthree_shop_api.review.dto.BookReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.dto.ReviewResponse;
import com.nhnacademy.ssacthree_shop_api.review.service.ReviewService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }


    @Test
    void testPostReviewBook() throws Exception {
        // Given
        String header = "testUser";
        Long bookId = 1L;
        Long orderId = 123L;
        ReviewRequestWithUrl reviewRequest = new ReviewRequestWithUrl(5, "Great Book", "Loved it!", "image_url");

        when(reviewService.postReviewBook(eq(header), eq(bookId), eq(orderId), any(ReviewRequestWithUrl.class)))
            .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // When & Then
        mockMvc.perform(post("/api/shop/members/reviews")
                .header("X-USER-ID", header)
                .param("book-id", String.valueOf(bookId))
                .param("order-id", String.valueOf(orderId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reviewRate\":5,\"reviewTitle\":\"Great Book\",\"reviewContent\":\"Loved it!\",\"reviewImage\":\"image_url\"}"))
            .andExpect(status().isCreated());

        verify(reviewService, times(1)).postReviewBook(eq(header), eq(bookId), eq(orderId), any(ReviewRequestWithUrl.class));
    }

    @Test
    void testAuthToWriteReview() throws Exception {
        // Given
        String header = "testUser";
        Long bookId = 1L;
        Long orderId = 123L;

        when(reviewService.authToWriteReview(eq(header), eq(bookId))).thenReturn(orderId);

        // When & Then
        mockMvc.perform(get("/api/shop/members/reviews/{book-id}", bookId)
                .header("X-USER-ID", header))
            .andExpect(status().isOk())
            .andExpect(content().string(String.valueOf(orderId)));

        verify(reviewService, times(1)).authToWriteReview(eq(header), eq(bookId));
    }

    @Test
    void testGetReviewsByMemberId() throws Exception {
        // Given
        String header = "testUser";
        List<MemberReviewResponse> reviews = List.of(
            new MemberReviewResponse(
                null,                // orderId
                null,                // bookId
                null,                // bookImageUrl
                "Book Title",        // bookTitle
                5,                   // reviewRate
                "Great Read",        // reviewTitle
                null,                // reviewContent
                null                 // reviewImageUrl
            ));

        when(reviewService.getReviewsByMemberId(eq(header))).thenReturn(reviews);

        // When & Then
        mockMvc.perform(get("/api/shop/members/reviews")
                .header("X-USER-ID", header))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].bookTitle").value("Book Title"))
            .andExpect(jsonPath("$[0].reviewRate").value(5));

        verify(reviewService, times(1)).getReviewsByMemberId(eq(header));
    }

    @Test
    void testGetReview() throws Exception {
        // Given
        String header = "testUser";
        Long orderId = 123L;
        Long bookId = 1L;
        ReviewResponse reviewResponse = new ReviewResponse( 5, "Great Read", "Loved it!", "image_url");

        when(reviewService.getReview(eq(header), eq(orderId), eq(bookId))).thenReturn(reviewResponse);

        // When & Then
        mockMvc.perform(get("/api/shop/members/reviews/update/{order-id}/{book-id}", orderId, bookId)
                .header("X-USER-ID", header))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reviewTitle").value("Great Read"))
            .andExpect(jsonPath("$.reviewRate").value(5));

        verify(reviewService, times(1)).getReview(eq(header), eq(orderId), eq(bookId));
    }
}