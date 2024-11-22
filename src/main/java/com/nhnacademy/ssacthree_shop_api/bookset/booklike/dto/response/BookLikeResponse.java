package com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response;

import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BookLikeResponse {
    private Long bookId;
    private Long customerId;
    @Setter
    private Long likeCount;

    public BookLikeResponse(BookLikeRequest bookLikeRequest) {
        this.bookId = bookLikeRequest.getBookId();
        this.customerId = bookLikeRequest.getCustomerId();
    }

    public BookLikeResponse(BookLikeRequest bookLikeRequest, Long likeCount) {
        this.bookId = bookLikeRequest.getBookId();
        this.customerId = bookLikeRequest.getCustomerId();
        this.likeCount = likeCount;
    }
}
